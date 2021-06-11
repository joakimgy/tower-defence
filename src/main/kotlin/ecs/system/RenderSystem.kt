package ecs.system

import Config.TILE_SIZE
import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Align
import ecs.component.*
import ecs.component.buildings.AttackTowerComponent
import ecs.component.buildings.BuildingBlockComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import screen.GameState
import utils.getCenterXY

class RenderSystem(
    private val batch: Batch,
    private val font: BitmapFont,
    private val camera: OrthographicCamera,
    private val gameState: GameState,
    private val assets: AssetManager
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
    private val circleRegion = assets[TextureAtlasAssets.TowerDefence].findRegion("circle")

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
            // Render textures related to interactive components
            entity[InteractiveComponent.mapper]?.let { interactiveComponent ->
                renderInteractiveComponent(interactiveComponent, transform, entity)
            }
        }
    }

    private fun renderInteractiveComponent(
        interactiveComponent: InteractiveComponent,
        transform: TransformComponent,
        entity: Entity
    ) {
        if (interactiveComponent.isClicked) {
            val bounds = Rectangle(
                Rectangle(
                    transform.bounds.x - TILE_SIZE * 2,
                    transform.bounds.y + TILE_SIZE,
                    TILE_SIZE * 5,
                    TILE_SIZE * 2
                )
            )
            entity[AttackTowerComponent.mapper]?.let {
                renderEntityInfo("Attack tower", bounds)
            }
            entity[BuildingBlockComponent.mapper]?.let {
                renderEntityInfo("Building block", bounds)
            }
            entity[PlayerComponent.mapper]?.let {
                entity[HealthComponent.mapper]?.let {
                    renderEntityInfo("Player has ${it.health} life points remaining.", bounds)
                }
            }
        }
        if (interactiveComponent.cursorIsHovering) {
            entity[AttackTowerComponent.mapper]?.let { tower ->
                if (!interactiveComponent.isClicked) {
                    drawTransparentCircle(transform, tower.range)
                }

            }
        }
    }

    private fun renderEntityInfo(text: String, bounds: Rectangle) {
        batch.draw(
            infoBoxTexture,
            bounds.x,
            bounds.y,
            bounds.width,
            bounds.height
        )
        val padding = TILE_SIZE / 4f
        val text = GlyphLayout(
            font,
            text,
            Color.WHITE,
            bounds.width - padding * 2,
            Align.left,
            true
        );
        font.draw(
            batch,
            text,
            bounds.x + padding,
            bounds.y + bounds.height - padding,
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

    private fun drawTransparentCircle(transform: TransformComponent, range: Float) {
        batch.setColor(0f, 0f, 0f, 0.2f)
        batch.draw(
            circleRegion,
            transform.bounds.getCenterXY().x - range,
            transform.bounds.getCenterXY().y - range,
            range * 2f,
            range * 2f
        )
        batch.color = Color.WHITE
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
