package screen

import assets.MusicAssets
import assets.get
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import ecs.system.*
import ktx.app.KtxScreen

class GameScreen(
    private val batch: Batch,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine
) : KtxScreen {

    override fun show() {
        // start the playback of the background music when the screen is shown
        assets[MusicAssets.Hype].apply { isLooping = true; volume = 0.00f }.play()

        // initialize entity engine
        engine.apply {
            addSystem(MoveSystem())
            addSystem(RenderSystem(batch, camera))
            addSystem(InputSystem(camera, assets))
            addSystem(SpawnSystem(assets))
            addSystem(ClickableSystem(batch, camera, assets))
            addSystem(AttackSystem(engine))
        }

    }

    override fun render(delta: Float) {
        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
    }

}


