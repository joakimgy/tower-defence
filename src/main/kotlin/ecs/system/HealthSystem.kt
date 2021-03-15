package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ecs.component.HealthComponent
import ktx.ashley.allOf
import ktx.ashley.get

class HealthSystem : IteratingSystem(allOf(HealthComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[HealthComponent.mapper]?.let { healthCmp ->
            if (healthCmp.health <= 0f) {
                engine.removeEntity(entity)
            }
        }
    }
}
