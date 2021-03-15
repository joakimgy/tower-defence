package ecs.system

import MAP_SIZE_X
import MAP_SIZE_Y
import TILE_SIZE
import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import utils.getCenterXY
import utils.toCoordinate

class InputSystem(
    private val camera: OrthographicCamera,
    assets: AssetManager,

    ) : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class, RenderComponent::class).get(),
) {

    private val touchPos = Vector3()
    private val turretRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("turret")
    private val buildingBlockRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("buildingBlock")

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val towerComponents =
            engine.getEntitiesFor(
                Family.one(BuildingBlockComponent::class.java, AttackTowerComponent::class.java).get()
            )
        val existingTowerBounds =
            towerComponents.mapNotNull { it[TransformComponent.mapper]?.bounds }

        entity[TransformComponent.mapper]?.let { transform ->
            entity[MoveComponent.mapper]?.let { move ->
                when {
                    Gdx.input.isKeyPressed(Input.Keys.A) -> move.speed.x = -200f
                    Gdx.input.isKeyPressed(Input.Keys.D) -> move.speed.x = 200f
                    else -> move.speed.x = 0f
                }
                when {
                    Gdx.input.isKeyPressed(Input.Keys.W) -> move.speed.y = 200f
                    Gdx.input.isKeyPressed(Input.Keys.S) -> move.speed.y = -200f
                    else -> move.speed.y = 0f
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                    camera.unproject(touchPos)
                    buildTower(Vector2(touchPos.x, touchPos.y), existingTowerBounds, AttackTowerComponent())
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                    camera.unproject(touchPos)
                    println("Touched ${touchPos.toCoordinate()}")
                }
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    buildTower(
                        Vector2(transform.bounds.getCenterXY().x, transform.bounds.getCenterXY().y),
                        existingTowerBounds,
                        BuildingBlockComponent()
                    )
                }
            }
        }
    }

    private fun buildTower(position: Vector2, existingTowerBounds: List<Rectangle>, tower: TowerComponent) {
        val towerBounds = Rectangle(
            position.x - position.x.rem(TILE_SIZE),
            position.y - position.y.rem(TILE_SIZE),
            TILE_SIZE,
            TILE_SIZE
        )
        val mapBounds = Rectangle(
            TILE_SIZE,
            TILE_SIZE,
            MAP_SIZE_X * TILE_SIZE,
            MAP_SIZE_Y * TILE_SIZE
        )
        if (!existingTowerBounds.contains(towerBounds) and mapBounds.contains(towerBounds.getCenterXY())) {

            engine.entity {
                with<TransformComponent> {
                    bounds.set(towerBounds)
                }
                with<ClickableComponent>()
                when (tower) {
                    is AttackTowerComponent -> {
                        with<RenderComponent> {
                            sprite.setRegion(turretRegion)
                        }
                        with<AttackTowerComponent>()
                    }
                    is BuildingBlockComponent -> {
                        with<RenderComponent> {
                            sprite.setRegion(buildingBlockRegion)
                        }
                        with<BuildingBlockComponent>()
                    }
                }
            }
        }
    }
}