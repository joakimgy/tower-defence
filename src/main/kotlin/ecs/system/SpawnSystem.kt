package ecs.system

import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import ecs.component.EnemyComponent
import ecs.component.MoveComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with

class SpawnSystem(assets: AssetManager) : IntervalSystem(10f) {
    private val enemyRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("enemy")
    private val dirtRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("dirt")

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        // Spawn map
        (1..10).forEach() { x ->
            (1..6).forEach { y ->
                engine?.entity {
                    with<TransformComponent> { bounds.set(x * 64f, y * 62f, 64f, 64f) }
                    with<RenderComponent> { sprite.setRegion(dirtRegion) }
                }
            }
        }
        // spawn an initial enemy when the system is added to the engine
        updateInterval()
    }

    override fun updateInterval() {
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
