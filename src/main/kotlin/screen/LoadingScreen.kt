package screen

import Game
import assets.TextureAtlasAssets
import assets.load
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreen(
    private val game: Game,
    private val batch: Batch,
    private val font: BitmapFont,
    private val assets: AssetManager,
    private val camera: OrthographicCamera
) : KtxScreen {

    override fun show() {
        TextureAtlasAssets.values().forEach { assets.load(it) }
    }

    override fun render(delta: Float) {
        // continue loading our assets
        assets.update()

        camera.update()
        batch.projectionMatrix = camera.combined

        batch.use {
            font.draw(it, "Welcome to Tower Defence!!! ", 100f, 150f)
            if (assets.isFinished) {
                font.draw(it, "Click anywhere to begin!", 100f, 100f)
            } else {
                font.draw(it, "Loading assets...", 100f, 100f)
            }
        }

        if (Gdx.input.isTouched && assets.isFinished) {
            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<GameScreen>()
        }
    }
}
