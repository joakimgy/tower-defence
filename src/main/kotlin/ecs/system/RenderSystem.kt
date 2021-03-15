package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import ecs.component.EnemyComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use


class RenderSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera,
    private val assets: AssetManager
) : SortedIteratingSystem(
    allOf(TransformComponent::class, RenderComponent::class).get(),
    // compareBy is used to render entities by their z-index (=player is drawn in the background; raindrops are drawn in the foreground)
    compareBy { entity: Entity -> entity[RenderComponent.mapper]?.z }
) {

    private val greenHealthBar =
        createTexture(1, 1, Color.GREEN.apply { a = 0.5f })
    private val redHealthBar =
        createTexture(1, 1, Color.RED.apply { a = 0.5f })
    private val healthBarWidth = 64f
    private val healthBarHeight = 4f

    override fun update(deltaTime: Float) {
        forceSort()
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        batch.projectionMatrix = camera.combined
        // draw all entities in one batch

        batch.use {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[RenderComponent.mapper]?.let { render ->
                batch.draw(render.sprite, transform.bounds.x, transform.bounds.y)
            }
            entity[EnemyComponent.mapper]?.let { enemy ->
                renderHealthBar(enemy, transform)
            }
        }
    }

    private fun renderHealthBar(enemy: EnemyComponent, transform: TransformComponent) {
        val healthPercentage = (enemy.health / enemy.maxHealth)
        batch.draw(
            greenHealthBar,
            transform.bounds.x,
            transform.bounds.y + 50f,
            healthBarWidth * healthPercentage,
            healthBarHeight
        )
        batch.draw(
            redHealthBar,
            transform.bounds.x + healthBarWidth * healthPercentage,
            transform.bounds.y + 50f,
            healthBarWidth * (1 - healthPercentage),
            healthBarHeight
        )
    }

    private fun createTexture(width: Int, height: Int, color: Color): Texture {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.fillRectangle(0, 0, width, height)
        val texture = Texture(pixmap)
        pixmap.dispose()
        return texture
    }
}
