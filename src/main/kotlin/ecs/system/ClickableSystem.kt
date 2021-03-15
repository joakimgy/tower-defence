package ecs.system

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
import com.badlogic.gdx.math.Vector3
import ecs.component.ClickableComponent
import ecs.component.TowerComponent
import ecs.component.TransformComponent
import ecs.component.getCenterXY
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use


class ClickableSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera,
    private val assets: AssetManager
) : IteratingSystem(
    allOf(ClickableComponent::class, TransformComponent::class).get(),
) {
    private val circleRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("circle")


    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[TowerComponent.mapper]?.let { tower ->
                if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
                    val clickPosition = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                    camera.unproject(clickPosition)
                    if (transform.bounds.contains(clickPosition.x, clickPosition.y)) {
                        batch.use {
                            drawTransparentCircle(transform, tower.range)
                        }
                    }
                }
            }
        }
    }

    fun drawTransparentCircle(transform: TransformComponent, range: Float) {
        batch.color = Color.DARK_GRAY
        batch.setColor(0f, 0f, 0f, 0.07f)
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
