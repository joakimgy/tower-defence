package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.get
import utils.toVector

class PathfindingSystem :
    IteratingSystem(allOf(EnemyComponent::class, TransformComponent::class, MoveComponent::class).get()) {


    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[EnemyComponent.mapper]?.let { enemy ->
            entity[TransformComponent.mapper]?.let { transform ->
                entity[MoveComponent.mapper]?.let { move ->
                    val destination = enemy.path.firstOrNull()

                    if (destination == null) {
                        move.speed.let {
                            it.x = 0f
                            it.y = 0f
                        }
                        val player =
                            engine.getEntitiesFor(
                                Family.all(PlayerComponent::class.java, HealthComponent::class.java).get()
                            ).firstOrNull()
                        if (player != null) {
                            val playerHealth = player[HealthComponent.mapper]
                            playerHealth?.let {
                                it.health -= 10f
                            }
                        }
                        return engine.removeEntity(entity)
                    }

                    if (transform.bounds.contains(destination.toVector())) {
                        enemy.path.removeFirst()
                    }

                    move.speed.let {
                        val newDestination = enemy.path.firstOrNull()
                        if (newDestination == null) {
                            it.x = 0f
                            it.y = 0f
                        } else {
                            it.x = newDestination.toVector().x - transform.bounds.x
                            it.y = newDestination.toVector().y - transform.bounds.y
                            it.setLength(enemy.speed)
                        }
                    }
                }
            }
        }
    }
}
