package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class MapSystem(
    map: Entity,
    private val batch: Batch,
    private val font: BitmapFont,
    private val camera: OrthographicCamera
) : IteratingSystem(allOf(MapComponent::class, RenderComponent::class).get()) {
    private val renderCmp = map[RenderComponent.mapper]!!
    private val mapCmp = map[MapComponent.mapper]!!

    override fun update(deltaTime: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        batch.projectionMatrix = camera.combined
        // draw all entities in one batch

        batch.use {
            super.update(deltaTime)
            mapCmp.blocks.keys.forEach {
                batch.draw(renderCmp.sprite.apply { rotate(1f) }, 10f.times(it.x), 10f.times(it.y))
            }
            font.draw(batch, "A map is born", 350f, 400f)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[RenderComponent.mapper]?.let { render ->
                batch.draw(render.sprite, transform.bounds.x, transform.bounds.y)
            }
        }
    }
}
