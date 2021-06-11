package ecs.component.buildings

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import ecs.component.InteractiveComponent
import ecs.component.RenderComponent
import ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with

enum class Tower {
    ATTACK_TOWER, BUILDING_BLOCK
}

fun buildBuildingBlock(engine: Engine, bounds: Rectangle, region: TextureRegion) {
    engine.entity {
        with<TransformComponent> {
            this.bounds.set(bounds)
        }
        with<InteractiveComponent>()
        with<RenderComponent> {
            sprite.setRegion(region)
        }
        with<BuildingBlockComponent>()

    }
}

fun buildAttackTower(engine: Engine, bounds: Rectangle, region: TextureRegion) {
    engine.entity {
        with<TransformComponent> {
            this.bounds.set(bounds)
        }
        with<InteractiveComponent>()
        with<RenderComponent> {
            sprite.setRegion(region)
        }
        with<AttackTowerComponent>()
    }
}
