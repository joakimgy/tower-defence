package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ecs.component.HealthComponent
import ecs.component.PlayerComponent
import ktx.ashley.allOf
import ktx.ashley.get

class HealthSystem(private val gameOver: () -> Unit) : IteratingSystem(allOf(HealthComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[HealthComponent.mapper]?.let { healthCmp ->
            entity[PlayerComponent.mapper]?.let {
                if (healthCmp.health <= 0f) {
                    gameOver()
                }
            }
            if (healthCmp.health <= 0f) {
                engine.removeEntity(entity)
            }
        }
    }
}
