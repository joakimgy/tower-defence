package ecs.system

import AlgorithmAStarImpl
import Coordinates
import MAP_SIZE_X
import MAP_SIZE_Y
import Route
import TILE_SIZE
import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import ecs.component.*
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import utils.adjacentCoordinates
import utils.toCoordinate


class SpawnSystem(assets: AssetManager) : IntervalSystem(3f) {
    private val enemyRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("enemy")
    private val tileDirtRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("tileDirt")
    private val playerRegion = assets[TextureAtlasAssets.BlackSmith].findRegion("dude")
    private val destinationRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("star")

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        spawnMap()
        spawnPlayer()
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
            with<HealthComponent>()
            with<TransformComponent> { bounds.set(800f / 2f, 480f / 2f, 24f, 32f) }
            with<MoveComponent>()
            with<RenderComponent> {
                z = 2
                sprite.setRegion(playerRegion)
            }
        }
    }

    private fun findEdges(occupiedCoordinates: List<Coordinates>): List<Route> {
        val allCoordinates = (0 until MAP_SIZE_X).map { x ->
            (0 until MAP_SIZE_Y).map { y ->
                Coordinates(x, y)
            }
        }.flatten()

        return allCoordinates.filter { !occupiedCoordinates.contains(it) }.map { coordinate ->
            coordinate.adjacentCoordinates()
                .filter { adjacent -> !occupiedCoordinates.contains(adjacent) }
                .filter { adjacent -> !(adjacent.x < 0 || adjacent.y < 0 || adjacent.x >= MAP_SIZE_X || adjacent.y >= MAP_SIZE_Y) }
                .map { adjacent -> Route(coordinate, adjacent) }
        }.flatten()
    }

    private fun spawnEnemy() {
        val towers = engine.getEntitiesFor(
            Family.one(BuildingBlockComponent::class.java, AttackTowerComponent::class.java).get()
        )
        val towerPositions = towers.mapNotNull { tower ->
            tower[TransformComponent.mapper]?.bounds?.toCoordinate()
        }
        val edges = findEdges(towerPositions)
        val path = AlgorithmAStarImpl(edges)
            .findPath(
                begin = Coordinates(0, 0),
                end = Coordinates(MAP_SIZE_X - 1, 0)
            ).first

        engine.entity {
            with<EnemyComponent> {
                this.path = path.toMutableList()
            }
            with<HealthComponent> {
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
