package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ecs.component.EnemyComponent
import ecs.component.MoveComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import utils.toVector

class PathfindingSystem :
    IteratingSystem(allOf(EnemyComponent::class, TransformComponent::class, MoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[EnemyComponent.mapper]?.let { enemy ->
            entity[TransformComponent.mapper]?.let { transform ->
                entity[MoveComponent.mapper]?.let { move ->
                    // Path finding
                    val destination = enemy.path.firstOrNull()
                    if (destination == null) {
                        println("Destination is null")
                        move.speed.let {
                            it.x = 0f
                            it.y = 0f
                        }
                        return
                    }

                    if (transform.bounds.contains(destination.toVector())) {
                        println("Destination is reached")
                        enemy.path.removeFirst()
                    }

                    move.speed.let {
                        val newDestination = enemy.path.firstOrNull()
                        if (newDestination == null) {
                            println("FINISHED")
                            it.x = 0f
                            it.y = 0f
                        } else {
                            println("New destination")
                            println("Enemy at ${transform.bounds} going to destination ${newDestination.toVector()}")
                            it.x = newDestination.toVector().x - transform.bounds.x
                            it.y = newDestination.toVector().y - transform.bounds.y
                            it.setLength(enemy.speed)
                            println("Speed $it")
                        }
                    }
                }
            }
        }
    }
}
