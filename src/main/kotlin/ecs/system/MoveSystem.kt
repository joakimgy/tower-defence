package ecs.system

import Config
import Config.TILE_SIZE
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import ecs.component.MoveComponent
import ecs.component.PlayerComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MoveSystem : IteratingSystem(allOf(MoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[MoveComponent.mapper]?.let { move ->
            entity[TransformComponent.mapper]?.let { transform ->
                // make sure the entities stay within the screen bounds
                transform.bounds.x =
                    MathUtils.clamp(
                        transform.bounds.x + move.speed.x * deltaTime,
                        TILE_SIZE,
                        Config.Screen.viewportWidth - TILE_SIZE * 2
                    )
                transform.bounds.y =
                    MathUtils.clamp(
                        transform.bounds.y + move.speed.y * deltaTime,
                        TILE_SIZE,
                        Config.Screen.viewportHeight - TILE_SIZE * 2
                    )
            }
            entity[PlayerComponent.mapper]?.let {
                when {
                    Gdx.input.isKeyPressed(Input.Keys.A) -> move.speed.x = -TILE_SIZE * 4
                    Gdx.input.isKeyPressed(Input.Keys.D) -> move.speed.x = TILE_SIZE * 4
                    else -> move.speed.x = 0f
                }
                when {
                    Gdx.input.isKeyPressed(Input.Keys.W) -> move.speed.y = TILE_SIZE * 4
                    Gdx.input.isKeyPressed(Input.Keys.S) -> move.speed.y = -TILE_SIZE * 4
                    else -> move.speed.y = 0f
                }
            }
        }
    }
}
