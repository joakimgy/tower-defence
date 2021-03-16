package screen

import Game
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ecs.system.*
import ktx.app.KtxScreen
import ktx.graphics.use
import utils.GameState


class GameScreen(
    private val game: Game,
    private val batch: Batch,
    private val font: BitmapFont,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine
) : KtxScreen {
    private val gameState = GameState(
        round = 1,
        enemiesToSpawn = 5,
        isBuilding = true,
        blocksRemaining = 5
    )

    override fun show() {
        // initialize entity engine
        engine.apply {
            addSystem(MoveSystem())
            addSystem(RenderSystem(batch, font, camera))
            addSystem(PlayerSystem(camera, assets, gameState))
            addSystem(SpawnSystem(gameState, assets))
            addSystem(ClickableSystem(batch, camera, assets))
            addSystem(AttackTowerSystem(assets))
            addSystem(ProjectileSystem())
            addSystem(EnemySystem())
            addSystem(HealthSystem(gameOver = {
                game.removeScreen<GameScreen>()
                dispose()
                game.setScreen<GameOverScreen>()
            }))
        }

    }

    override fun render(delta: Float) {
        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
        // Show interface
        batch.use {
            font.draw(batch, "Round: ${gameState.round}", 200f, 32f * 15f - 16f)
            if (gameState.isBuilding) {
                font.draw(batch, "Blocks remaining: ${gameState.blocksRemaining}", 300f, 32f * 15f - 16f)
            }
        }
    }

}


