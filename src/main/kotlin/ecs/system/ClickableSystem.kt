package ecs.system

import assets.TextureAtlasAssets
import assets.get
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector3
import ecs.component.ClickableComponent
import ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use


class ClickableSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera,
    private val assets: AssetManager
) : IteratingSystem(
    allOf(ClickableComponent::class, TransformComponent::class).get(),
) {
    private val circle = assets[TextureAtlasAssets.TowerDefence].findRegion("circle")

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
                val clickPosition = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(clickPosition)
                if (transform.bounds.contains(clickPosition.x, clickPosition.y)) {
                    println("Touched$entity")
                    batch.use {
                        batch.draw(circle, transform.bounds.x, transform.bounds.y)
                    }
                }
            }
        }
    }
}