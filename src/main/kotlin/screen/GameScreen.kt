package screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.app.KtxScreen

class GameScreen(
    private val batch: Batch,
    private val font: BitmapFont,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine
) : KtxScreen {
    private var player = Player(40f, 80f)

    override fun render(delta: Float) {
        handleInput()
        logic()
        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
    }

    private fun handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player = Player(player.x - 5f, player.y)
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player = Player(player.x + 5f, player.y)
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player = Player(player.x, player.y + 5f)
        }else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player = Player(player.x, player.y - 5f)
        }
    }
    private fun logic() { }


    override fun show() {
        // start the playback of the background music when the screen is shown
        //assets[MusicAssets.Rain].apply { isLooping = true }.play()
        // set bucket sprite
        //bucket[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("bucket"))
        // initialize entity engine
        engine.apply {
            // add systems
            //addSystem(SpawnSystem(assets))
            //addSystem(MoveSystem())
            //addSystem(RenderSystem(bucket, batch, font, camera))
            // add CollisionSystem last as it removes entities and this should always
            // happen at the end of an engine update frame
            //addSystem(CollisionSystem(bucket, assets))
        }
    }
}

data class Player(val x: Float, val y: Float)