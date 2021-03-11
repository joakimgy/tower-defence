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
        with<TransformComponent> { bounds.set(800f / 2f, 480f / 2f, 24f, 32f) }
        with<MoveComponent>()
        with<RenderComponent> { z = 1 }
    }

    private val dirtBlocks = (1..10).map { x ->
        (1..6).map { y ->
            engine.entity {
                with<TransformComponent> { bounds.set(x * 64f, y * 62f, 64f, 64f) }
                with<RenderComponent>()
            }
        }
    }


    override fun show() {
        // start the playback of the background music when the screen is shown
        assets[MusicAssets.Hype].apply { isLooping = true; volume = 0.00f }.play()
        // set sprites
        player[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.BlackSmith].findRegion("dude"))
        dirtBlocks.flatten().forEach { block ->
            block[RenderComponent.mapper]?.sprite?.setRegion(
                assets[TextureAtlasAssets.TowerDefence].findRegion(
                    "dirt"
                )
            )
        }


        // initialize entity engine
        engine.apply {
            addSystem(MoveSystem())
            addSystem(RenderSystem(player, batch, font, camera))
        }

    }

    override fun render(delta: Float) {
        handleInput()
        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
    }

    // create the touchPos to store mouse click position
    private val touchPos = Vector3()

    private fun handleInput() {
        // process user input
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            player[TransformComponent.mapper]?.let { transform ->
                transform.bounds.x = touchPos.x - 24f / 2f
                transform.bounds.y = touchPos.y - 32f / 2f
            }
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.x = -200f
            }
            Gdx.input.isKeyPressed(Input.Keys.D) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.x = 200f
            }
            else -> player[MoveComponent.mapper]?.let { move ->
                move.speed.x = 0f
            }
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.W) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.y = 200f
            }
            Gdx.input.isKeyPressed(Input.Keys.S) -> player[MoveComponent.mapper]?.let { move ->
                move.speed.y = -200f
            }
            else -> player[MoveComponent.mapper]?.let { move ->
                move.speed.y = 0f
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            engine.entity {
                with<BlockComponent>()
                with<TransformComponent> {
                    val posX = player[TransformComponent.mapper]!!.bounds.x
                    val posY = player[TransformComponent.mapper]!!.bounds.y
                    bounds.set(posX - posX.rem(64f), posY - posY.rem(64f), 24f, 32f)
                }
                with<MoveComponent>()
                with<RenderComponent> {
                    sprite.setRegion(assets[TextureAtlasAssets.TowerDefence].findRegion("buildingBlock"))
                }
            }
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            engine.entity {
                with<BlockComponent>()
                with<TransformComponent> {
                    bounds.set(touchPos.x - touchPos.x.rem(64f), touchPos.y - touchPos.y.rem(64f), 24f, 32f)
                }
                with<MoveComponent>()
                with<RenderComponent> {
                    sprite.setRegion(assets[TextureAtlasAssets.TowerDefence].findRegion("buildingBlock"))
                }
            }
        }
    }


}
