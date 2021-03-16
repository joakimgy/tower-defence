package ecs.system

import TILE_SIZE
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import ecs.component.MoveComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MoveSystem : IteratingSystem(allOf(MoveComponent::class, TransformComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[MoveComponent.mapper]?.let { move ->
            entity[TransformComponent.mapper]?.let { transform ->
                // make sure the entities stay within the screen bounds
                transform.bounds.x =
                    MathUtils.clamp(transform.bounds.x + move.speed.x * deltaTime, TILE_SIZE, 800f - TILE_SIZE * 2)
                transform.bounds.y =
                    MathUtils.clamp(transform.bounds.y + move.speed.y * deltaTime, TILE_SIZE, 480f - TILE_SIZE * 2)
            }
        }
    }
}
