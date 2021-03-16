package ecs.system

import TILE_SIZE
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ecs.component.HealthComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ecs.component.buildings.BuildingInfoComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import utils.GameState


class RenderSystem(
    private val batch: Batch,
    private val font: BitmapFont,
    private val camera: OrthographicCamera,
    private val gameState: GameState
) : SortedIteratingSystem(
    allOf(RenderComponent::class, TransformComponent::class).get(),
    // compareBy is used to render entities by their z-index (=player is drawn in the background; raindrops are drawn in the foreground)
    compareBy { entity: Entity -> entity[RenderComponent.mapper]?.z }
) {

    private val greenHealthBarTexture =
        createTexture(1, 1, Color.GREEN.apply { a = 0.5f })
    private val redHealthBarTexture =
        createTexture(1, 1, Color.RED.apply { a = 0.5f })
    private val infoBoxTexture = createTexture(1, 1, Color.DARK_GRAY.apply { a = 0.5f })

    private val healthBarWidth = TILE_SIZE
    private val healthBarHeight = 4f

    override fun update(deltaTime: Float) {
        forceSort()
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        batch.projectionMatrix = camera.combined
        // draw all entities in one batch

        batch.use {
            renderGameInterface()
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            // Render all sprites
            entity[RenderComponent.mapper]?.let { render ->
                if (render.sprite.texture != null) {
                    batch.draw(render.sprite, transform.bounds.x, transform.bounds.y, TILE_SIZE, TILE_SIZE)
                }
            }
            // Render health bar for enemies
            entity[HealthComponent.mapper]?.let { health ->
                renderHealthBar(health, transform)
            }
            // Render building information
            entity[BuildingInfoComponent.mapper]?.let { info ->
                renderBuildingInfo(info, transform)
            }
        }
    }

    private fun renderBuildingInfo(info: BuildingInfoComponent, transform: TransformComponent) {
        batch.draw(
            infoBoxTexture,
            transform.bounds.x,
            transform.bounds.y,
            transform.bounds.width,
            transform.bounds.height
        )
        font.draw(
            batch,
            "${info.upgrades}",
            transform.bounds.x + TILE_SIZE / 2f,
            transform.bounds.y + transform.bounds.height - TILE_SIZE / 2f
        )
    }

    private fun renderHealthBar(health: HealthComponent, transform: TransformComponent) {
        val healthPercentage = (health.health / health.maxHealth)
        batch.draw(
            greenHealthBarTexture,
            transform.bounds.x,
            transform.bounds.y + TILE_SIZE,
            healthBarWidth * healthPercentage,
            healthBarHeight
        )
        batch.draw(
            redHealthBarTexture,
            transform.bounds.x + healthBarWidth * healthPercentage,
            transform.bounds.y + TILE_SIZE,
            healthBarWidth * (1 - healthPercentage),
            healthBarHeight
        )
    }

    private fun renderGameInterface() {
        font.draw(batch, "Round: ${gameState.round}", 200f, 32f * 15f - 16f)
        if (gameState.isBuilding) {
            font.draw(batch, "Blocks remaining: ${gameState.blocksRemaining}", 300f, 32f * 15f - 16f)
        }
    }

    /**
     * Create a rectangle texture
     * @param width Width of texture
     * @param height Heigth of texture
     * @param color Color of texture
     */
    private fun createTexture(width: Int, height: Int, color: Color): Texture {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.fillRectangle(0, 0, width, height)
        val texture = Texture(pixmap)
        pixmap.dispose()
        return texture
    }
}
