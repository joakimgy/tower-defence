import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import screen.GameOverScreen
import screen.GameScreen
import screen.LoadingScreen

const val MAP_SIZE_X = 23
const val MAP_SIZE_Y = 13
const val TILE_SIZE = 32f

class Game : KtxGame<KtxScreen>() {
    private val context = Context()


    override fun create() {
        context.register {
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton(BitmapFont())
            bindSingleton(AssetManager())
            // The camera ensures we can render using our target resolution of 800x480
            //    pixels no matter what the screen resolution is.
            bindSingleton(OrthographicCamera().apply { setToOrtho(false, 800f, 480f) })
            bindSingleton(PooledEngine())

            addScreen(LoadingScreen(this@Game, inject(), inject(), inject(), inject()))
            addScreen(GameScreen(this@Game, inject(), inject(), inject(), inject()))
            addScreen(GameOverScreen(inject(), inject(), inject(), inject()))


        }
        setScreen<LoadingScreen>()
        super.create()
    }

    override fun dispose() {
        context.dispose()
        super.dispose()
    }

}
