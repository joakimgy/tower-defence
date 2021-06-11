package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector3
import ecs.component.InteractiveComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class InteractiveSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera,
) : IteratingSystem(
    allOf(InteractiveComponent::class).get(),
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[InteractiveComponent.mapper]?.let { interactive ->
            entity[TransformComponent.mapper]?.let { transform ->
                val cursorPosition = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(cursorPosition)
                // Update state that shows if the entity is hovered or clicked.
                if (transform.bounds.contains(cursorPosition.x, cursorPosition.y)) {
                    interactive.cursorIsHovering = true
                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        interactive.isClicked = true
                    }
                } else {
                    interactive.cursorIsHovering = false
                    interactive.isClicked = false
                }
            }
        }
    }
}