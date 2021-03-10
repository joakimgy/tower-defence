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
        with<RenderComponent>()
    }

    override fun render(delta: Float) {
        handleInput()
        logic()
        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
    }

    private fun handleInput() {
        /*
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player = Player(player.x - 5f, player.y)
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player = Player(player.x + 5f, player.y)
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player = Player(player.x, player.y + 5f)
        }else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player = Player(player.x, player.y - 5f)
        }
         */
    }
    private fun logic() { }


    override fun show() {
        // start the playback of the background music when the screen is shown
        assets[MusicAssets.Rain].apply { isLooping = true; volume = 0.01f }.play()
        // set player sprite
        player[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("player"))
        // initialize entity engine
        engine.apply {
            // add systems
            //addSystem(SpawnSystem(assets))
            addSystem(MoveSystem())
            addSystem(RenderSystem(player, batch, font, camera))
            // add CollisionSystem last as it removes entities and this should always
            // happen at the end of an engine update frame
            //addSystem(CollisionSystem(bucket, assets))
        }
    }
}
