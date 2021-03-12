package screen

import assets.MusicAssets
import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ecs.component.MoveComponent
import ecs.component.PlayerComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ecs.system.InputSystem
import ecs.system.MoveSystem
import ecs.system.RenderSystem
import ecs.system.SpawnSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

class GameScreen(
    private val batch: Batch,
    private val font: BitmapFont,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine
) : KtxScreen {

    private val player = engine.entity {
        with<PlayerComponent>()
        with<TransformComponent> { bounds.set(800f / 2f, 480f / 2f, 24f, 32f) }
        with<MoveComponent>()
        with<RenderComponent> { z = 2 }
    }


    override fun show() {
        // start the playback of the background music when the screen is shown
        assets[MusicAssets.Hype].apply { isLooping = true; volume = 0.00f }.play()
        // set sprites
        player[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.BlackSmith].findRegion("dude"))

        // initialize entity engine
        engine.apply {
            addSystem(MoveSystem())
            addSystem(RenderSystem(player, batch, font, camera))
            addSystem(InputSystem(camera, assets))
            addSystem(SpawnSystem(assets))
        }

    }

    override fun render(delta: Float) {
        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
    }

}


