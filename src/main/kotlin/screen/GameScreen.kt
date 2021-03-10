package screen

import assets.MusicAssets
import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import ecs.component.*
import ecs.system.MoveSystem
import ecs.system.RenderSystem
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
        with<TransformComponent> { bounds.set(800f / 2f - 64f / 2f, 20f, 64f, 64f) }
        with<MoveComponent>()
        with<RenderComponent> { z = 1 }
    }

    private val map = engine.entity {
        with<MapComponent>()
        with<TransformComponent> { bounds.set(800f / 2f - 64f / 2f, 120f, 126f, 126f) }
        with<MoveComponent>()
        with<RenderComponent>()
    }

    // create the touchPos to store mouse click position
    private val touchPos = Vector3()

    override fun render(delta: Float) {
        handleInput()
        logic()
        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
    }

    private fun handleInput() {
        // process user input
        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            player[TransformComponent.mapper]?.let { transform ->
                transform.bounds.x = touchPos.x - 64f / 2f
                transform.bounds.y = touchPos.y - 64f / 2f
            }
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.x = -200f
            }
            Gdx.input.isKeyPressed(Input.Keys.D) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.x = 200f
            }
            Gdx.input.isKeyPressed(Input.Keys.W) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.y = 200f
            }
            Gdx.input.isKeyPressed(Input.Keys.S) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.y = -200f
            }
            else -> player[MoveComponent.mapper]?.let { move ->
                move.speed.x = 0f
                move.speed.y = 0f
            }
        }
    }

    private fun logic() {}


    override fun show() {
        // start the playback of the background music when the screen is shown
        assets[MusicAssets.Rain].apply { isLooping = true; volume = 0.01f }.play()
        // set player sprite
        player[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("player"))
        // set map
        map[RenderComponent.mapper]?.sprite?.apply {
            setRegion(assets[TextureAtlasAssets.Map].findRegion("tile"))
            setScale(10f)
        }

        // initialize entity engine
        engine.apply {
            addSystem(MoveSystem())
            addSystem(RenderSystem(player, batch, font, camera))
        }
    }
}
