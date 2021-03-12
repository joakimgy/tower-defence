package ecs.system

import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import ecs.component.MoveComponent
import ecs.component.PlayerComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

class InputSystem(
    private val camera: OrthographicCamera,
    private val assets: AssetManager,

    ) : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class, RenderComponent::class).get(),
) {

    private val touchPos = Vector3()

    override fun processEntity(entity: Entity, deltaTime: Float) {

        entity[TransformComponent.mapper]?.let { transform ->
            entity[MoveComponent.mapper]?.let { move ->
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                    camera.unproject(touchPos)
                    transform.bounds.x = touchPos.x - 24f / 2f
                    transform.bounds.y = touchPos.y - 32f / 2f
                }
                when {
                    Gdx.input.isKeyPressed(Input.Keys.A) -> move.speed.x = -200f
                    Gdx.input.isKeyPressed(Input.Keys.D) -> move.speed.x = 200f
                    else -> move.speed.x = 0f
                }
                when {
                    Gdx.input.isKeyPressed(Input.Keys.W) -> move.speed.y = 200f
                    Gdx.input.isKeyPressed(Input.Keys.S) -> move.speed.y = -200f
                    else -> move.speed.y = 0f
                }
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    engine.entity {
                        with<TransformComponent> {
                            val posX = transform.bounds.x
                            val posY = transform.bounds.y
                            bounds.set(posX - posX.rem(64f), posY - posY.rem(64f), 24f, 32f)
                        }
                        with<MoveComponent>()
                        with<RenderComponent> {
                            sprite.setRegion(assets[TextureAtlasAssets.TowerDefence].findRegion("turret"))
                        }
                    }
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                    camera.unproject(touchPos)
                    engine.entity {
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
    }
}