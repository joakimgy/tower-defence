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
import ecs.component.EnemyComponent
import ecs.component.PlayerComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ecs.component.buildings.*
import ktx.ashley.allOf
import ktx.ashley.get
import utils.getCenterXY
import screen.GameState

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


        // Should this input logic be somewhere else?
        entity[TransformComponent.mapper]?.let { transform ->
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(touchPos)
                buildTowerIfPossible(
                    Vector2(touchPos.x, touchPos.y),
                    Tower.ATTACK_TOWER,
                    gameState
                )
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(touchPos)
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                buildTowerIfPossible(
                    Vector2(transform.bounds.getCenterXY().x, transform.bounds.getCenterXY().y),
                    Tower.BUILDING_BLOCK,
                    gameState
                )

            }
        }
    }

    private fun buildTowerIfPossible(
        position: Vector2,
        tower: Tower,
        gameState: GameState
    ) {
        val towerBounds = Rectangle(
            position.x - position.x.rem(TILE_SIZE),
            position.y - position.y.rem(TILE_SIZE),
            TILE_SIZE,
            TILE_SIZE
        )
        if (canBuildAtPosition(towerBounds)) {
            when (tower) {
                Tower.ATTACK_TOWER -> buildAttackTower(engine, towerBounds, turretRegion)
                Tower.BUILDING_BLOCK -> buildBuildingBlock(engine, towerBounds, buildingBlockRegion)
            }
            gameState.blocksRemaining -= 1
        }
    }

    private fun canBuildAtPosition(position: Rectangle): Boolean {
        val towerComponents =
            engine.getEntitiesFor(
                Family.one(BuildingBlockComponent::class.java, AttackTowerComponent::class.java).get()
            )
        val existingTowerPositions =
            towerComponents.mapNotNull { it[TransformComponent.mapper]?.bounds }
        val enemies = engine.getEntitiesFor(Family.one(EnemyComponent::class.java).get())

        val mapBounds = Rectangle(
            TILE_SIZE,
            TILE_SIZE,
            MAP_SIZE_X * TILE_SIZE,
            MAP_SIZE_Y * TILE_SIZE
        )

        val enemiesAreAlive = enemies.firstOrNull() != null
        val isOccupied = existingTowerPositions.contains(position)
        val isOutOfBounds = !mapBounds.contains(position.getCenterXY())
        return !enemiesAreAlive && !isOccupied && !isOutOfBounds && gameState.isBuilding
    }
}