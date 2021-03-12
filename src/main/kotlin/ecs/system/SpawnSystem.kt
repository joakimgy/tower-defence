package ecs.system

import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with

class SpawnSystem(assets: AssetManager) : IntervalSystem(10f) {
    private val enemyRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("enemy")
    private val dirtRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("dirt")
    private val playerRegion = assets[TextureAtlasAssets.BlackSmith].findRegion("dude")

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        spawnMap()
        spawnPlayer()
        spawnEnemy()
    }

    override fun updateInterval() {
        spawnEnemy()
    }


    /*
     * Help functions for spawning various entities
     */

    private fun spawnMap() {
        (1..10).forEach() { x ->
            (1..6).forEach { y ->
                engine.entity {
                    with<TransformComponent> { bounds.set(x * 64f, y * 62f, 64f, 64f) }
                    with<RenderComponent> { sprite.setRegion(dirtRegion) }
                }
            }
        }
    }

    private fun spawnPlayer() {
        engine.entity {
            with<PlayerComponent>()
            with<TransformComponent> { bounds.set(800f / 2f, 480f / 2f, 24f, 32f) }
            with<MoveComponent>()
            with<RenderComponent> {
                z = 2
                sprite.setRegion(playerRegion)
            }
        }
    }

    private fun spawnEnemy() {
        engine.entity {
            with<EnemyComponent>()
            with<RenderComponent> {
                z = 1
                sprite.setRegion(enemyRegion)
            }
            with<MoveComponent> {
                speed.x = 10f
                speed.y = 10f
            }
            with<TransformComponent> { bounds.set(64f, 62f, 64f, 64f) }
        }
    }
}
