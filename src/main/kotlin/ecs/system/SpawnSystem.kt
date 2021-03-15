package ecs.system

import MAP_SIZE_X
import MAP_SIZE_Y
import TILE_SIZE
import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with


class SpawnSystem(assets: AssetManager) : IntervalSystem(3f) {
    private val enemyRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("enemy")
    private val tileDirtRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("tileDirt")
    private val playerRegion = assets[TextureAtlasAssets.BlackSmith].findRegion("dude")
    private val destinationRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("star")

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
        (1..MAP_SIZE_X).forEach() { x ->
            (1..MAP_SIZE_Y).forEach { y ->
                engine.entity {
                    with<TransformComponent> { bounds.set(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE) }
                    with<RenderComponent> {
                        sprite.setRegion(tileDirtRegion)
                    }
                }
            }
        }
        engine.entity {
            with<TransformComponent> { bounds.set(MAP_SIZE_X * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE) }
            with<RenderComponent> {
                sprite.setRegion(destinationRegion)
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
            with<EnemyComponent> {
                health = maxHealth
            }
            with<RenderComponent> {
                z = 1
                sprite.setRegion(enemyRegion)
            }
            with<MoveComponent> {
                speed.x = 10f
                speed.y = 10f
            }
            with<TransformComponent> { bounds.set(TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE) }
        }
    }
}
