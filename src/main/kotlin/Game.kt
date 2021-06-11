import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import screen.GameOverScreen
import screen.GameScreen
import screen.LoadingScreen


class Game : KtxGame<KtxScreen>() {
    private val context = Context()

    override fun create() {
        val file = Gdx.files.internal("src/main/kotlin/assets/fonts/Raleway-SemiBold.ttf")
        val generator = FreeTypeFontGenerator(file)
        val parameter = FreeTypeFontParameter().apply {
            size = 30
        }
        val bitmapFont = generator.generateFont(parameter)
        generator.dispose()

        context.register {
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton(bitmapFont)
            bindSingleton(AssetManager())
            // The camera ensures we can render using our target resolution of 800x480
            //    pixels no matter what the screen resolution is.
            bindSingleton(OrthographicCamera().apply {
                setToOrtho(
                    false,
                    Config.Screen.viewportWidth,
                    Config.Screen.viewportHeight
                )
            })
            bindSingleton(PooledEngine())

            addScreen(LoadingScreen(this@Game, inject(), inject(), inject(), inject()))
            addScreen(GameScreen(this@Game, inject(), inject(), inject(), inject(), inject()))
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
