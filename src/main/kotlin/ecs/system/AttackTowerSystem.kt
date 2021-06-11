package ecs.system

import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2
import ecs.component.*
import ecs.component.buildings.AttackTowerComponent
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import utils.getCenterXY

class AttackTowerSystem(
    assets: AssetManager
) : IntervalIteratingSystem(
    allOf(AttackTowerComponent::class, TransformComponent::class).get(), 0.5f
) {

    private val projectileRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("projectile")

    override fun processEntity(entity: Entity) {
        val enemyEntities = engine.getEntitiesFor(Family.all(EnemyComponent::class.java).get())

        entity[TransformComponent.mapper]?.let { towerTransform ->
            entity[AttackTowerComponent.mapper]?.let { tower ->
                val towerRange = Circle().apply {
                    radius = tower.range
                    x = towerTransform.bounds.getCenterXY().x
                    y = towerTransform.bounds.getCenterXY().y
                }
                enemyEntities.forEach { enemyEntity ->
                    val enemyTransform = enemyEntity[TransformComponent.mapper]
                    val enemyComponent = enemyEntity[EnemyComponent.mapper]
                    if (enemyComponent == null || enemyTransform == null) {
                        return@forEach
                    }

                    if (towerRange.contains(
                            enemyTransform.bounds.getCenterXY().x,
                            enemyTransform.bounds.getCenterXY().y
                        )
                    ) {
                        engine.entity {
                            with<ProjectileComponent> {
                                this.targetEntity = enemyEntity
                                this.damage = tower.damage
                            }
                            with<MoveComponent> {
                                this.speed = Vector2(
                                    enemyTransform.bounds.x - towerTransform.bounds.x,
                                    enemyTransform.bounds.y - towerTransform.bounds.y
                                ).setLength(100f)
                            }
                            with<TransformComponent> { bounds.set(towerTransform.bounds) }
                            with<RenderComponent> {
                                z = 2
                                sprite.setRegion(projectileRegion)
                            }
                        }
                        return
                    }
                }
            }
        }
    }
}
