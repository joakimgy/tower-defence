package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Circle
import ecs.component.EnemyComponent
import ecs.component.TowerComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.contains
import ktx.ashley.get
import utils.getCenterXY


class AttackSystem(
    private val engine: PooledEngine
) : IteratingSystem(
    allOf(TowerComponent::class, TransformComponent::class).get(),
) {


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val enemyEntities = engine.entities.filter { it.contains(EnemyComponent.mapper) }

        entity[TransformComponent.mapper]?.let { transform ->
            entity[TowerComponent.mapper]?.let { tower ->
                val range = Circle().apply {
                    radius = tower.range
                    x = transform.bounds.getCenterXY().x
                    y = transform.bounds.getCenterXY().y
                }
                enemyEntities.forEach {
                    val enemy = it[TransformComponent.mapper]
                    if (enemy != null && range.contains(
                            enemy.bounds.getCenterXY().x,
                            enemy.bounds.getCenterXY().y
                        )
                    ) {
                        it.removeAll()
                    }
                }
            }
        }
    }
}
