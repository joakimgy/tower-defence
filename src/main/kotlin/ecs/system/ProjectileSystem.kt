package ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import ecs.component.EnemyComponent
import ecs.component.MoveComponent
import ecs.component.ProjectileComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import utils.getCenterXY


class ProjectileSystem() : IteratingSystem(
    allOf(ProjectileComponent::class, TransformComponent::class, MoveComponent::class).get()
) {


    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[ProjectileComponent.mapper]?.let { projectile ->
            entity[TransformComponent.mapper]?.let { transform ->
                entity[MoveComponent.mapper]?.let { move ->
                    val enemyCmp = projectile.enemyEntity[EnemyComponent.mapper]
                    val enemyTransformCmp = projectile.enemyEntity[TransformComponent.mapper]
                    // Check that enemy is still alive
                    if (enemyCmp == null || enemyTransformCmp == null) {
                        engine.removeEntity(entity)
                        return
                    }

                    // Update trajectory
                    val enemyPos = enemyTransformCmp.bounds
                    val projectilePos = transform.bounds
                    move.speed.let {
                        it.x = enemyPos.x - projectilePos.x
                        it.y = enemyPos.y - projectilePos.y
                        it.setLength(150f)
                    }

                    // Handle hit
                    val hitbox = Rectangle(enemyPos.getCenterXY().x - 5f, enemyPos.getCenterXY().y - 5f, 10f, 10f)
                    if (hitbox.contains(projectilePos.getCenterXY().x, projectilePos.getCenterXY().y)) {
                        enemyCmp.health -= projectile.damage
                        if (enemyCmp.health <= 0f) {
                            engine.removeEntity(projectile.enemyEntity)
                        }
                        engine.removeEntity(entity)
                    }
                }
            }
        }
    }
}