package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.Circle
import ecs.component.EnemyComponent
import ecs.component.TowerComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import utils.getCenterXY


class AttackSystem(
    private val engine: PooledEngine
) : IntervalIteratingSystem(
    allOf(TowerComponent::class, TransformComponent::class).get(), 0.5f
) {


    override fun processEntity(entity: Entity) {
        val enemyEntities = engine.getEntitiesFor(Family.all(EnemyComponent::class.java).get())

        entity[TransformComponent.mapper]?.let { towerTransform ->
            entity[TowerComponent.mapper]?.let { tower ->
                val towerRange = Circle().apply {
                    radius = tower.range
                    x = towerTransform.bounds.getCenterXY().x
                    y = towerTransform.bounds.getCenterXY().y
                }
                enemyEntities.forEach {
                    val enemyTransform = it[TransformComponent.mapper]
                    val enemyComponent = it[EnemyComponent.mapper]
                    if (enemyComponent == null || enemyTransform == null) {
                        return@forEach
                    }

                    if (towerRange.contains(
                            enemyTransform.bounds.getCenterXY().x,
                            enemyTransform.bounds.getCenterXY().y
                        )
                    ) {
                        enemyComponent.health -= tower.damage
                        if (enemyComponent.health <= 0f) {
                            it.removeAll()
                        } else {
                            println("Tower attacked enemy for a ${tower.damage}. ${enemyComponent.health} health remains.")
                        }
                    }
                }
            }
        }
    }
}
