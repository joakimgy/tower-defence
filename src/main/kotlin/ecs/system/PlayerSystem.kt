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
import ecs.component.ClickableComponent
import ecs.component.PlayerComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ecs.component.buildings.AttackTowerComponent
import ecs.component.buildings.BuildingBlockComponent
import ecs.component.buildings.Towers
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import utils.GameState
import utils.getCenterXY

class PlayerSystem(
    private val camera: OrthographicCamera,
    assets: AssetManager,
    private val gameState: GameState,

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
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(touchPos)
                buildTower(Vector2(touchPos.x, touchPos.y), existingTowerBounds, Towers.ATTACK_TOWER, gameState)
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(touchPos)
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                buildTower(
                    Vector2(transform.bounds.getCenterXY().x, transform.bounds.getCenterXY().y),
                    existingTowerBounds,
                    Towers.BUILDING_BLOCK,
                    gameState
                )

            }
        }
    }

    private fun buildTower(
        position: Vector2,
        existingTowerBounds: List<Rectangle>,
        tower: Towers,
        gameState: GameState
    ) {
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
        // Check that tile is inside map, doesn't have a tower on it, and that game state allows building
        if (existingTowerBounds.contains(towerBounds)
            || !mapBounds.contains(towerBounds.getCenterXY())
            || !gameState.isBuilding
        ) {
            return
        }

        engine.entity {
            with<TransformComponent> {
                bounds.set(towerBounds)
            }
            with<ClickableComponent>()
            when (tower) {
                Towers.ATTACK_TOWER -> {
                    with<RenderComponent> {
                        sprite.setRegion(turretRegion)
                    }
                    with<AttackTowerComponent>()
                }
                Towers.BUILDING_BLOCK -> {
                    with<RenderComponent> {
                        sprite.setRegion(buildingBlockRegion)
                    }
                    with<BuildingBlockComponent>()
                }
            }
        }

        gameState.blocksRemaining -= 1
        if (gameState.blocksRemaining == 0) {
            gameState.isBuilding = false
            gameState.blocksRemaining = 5
        }
    }
}