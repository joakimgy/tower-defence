package ecs.system

import Config.TILE_SIZE
import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import ecs.component.ClickableComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ecs.component.buildings.AttackTowerComponent
import ecs.component.buildings.BuildingBlockComponent
import ecs.component.buildings.BuildingInfoComponent
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import utils.getCenterXY


class ClickableSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera,
    assets: AssetManager
) : IteratingSystem(
    allOf(ClickableComponent::class).get(),
) {
    private val circleRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("circle")

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
                val clickPosition = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(clickPosition)
                if (!transform.bounds.contains(clickPosition.x, clickPosition.y)) {
                    return
                }
                batch.use {
                    entity[AttackTowerComponent.mapper]?.let { attackTower ->
                        drawTransparentCircle(transform, attackTower.range)
                    }
                    entity[BuildingBlockComponent.mapper]?.let {
                        engine.entity {
                            with<BuildingInfoComponent> {
                                this.entity = entity
                            }
                            with<ClickableComponent>()
                            with<RenderComponent>()
                            with<TransformComponent> {
                                bounds.set(
                                    Rectangle(
                                        transform.bounds.x - TILE_SIZE * 2,
                                        transform.bounds.y + TILE_SIZE,
                                        TILE_SIZE * 5,
                                        TILE_SIZE * 2
                                    )
                                )
                            }
                        }
                    }
                    entity[BuildingInfoComponent.mapper]?.let {
                        engine.removeEntity(entity)
                    }
                }
            }
        }
    }

    private fun drawTransparentCircle(transform: TransformComponent, range: Float) {
        batch.setColor(0f, 0f, 0f, 0.2f)
        batch.draw(
            circleRegion,
            transform.bounds.getCenterXY().x - range,
            transform.bounds.getCenterXY().y - range,
            range * 2f,
            range * 2f
        )
        batch.color = Color.WHITE
    }
}