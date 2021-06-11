package ecs.component.buildings

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ecs.component.TransformComponent
import ktx.ashley.get
import ktx.ashley.mapperFor

class BuildingBlockComponent : Component {
    companion object {
        val mapper = mapperFor<BuildingBlockComponent>()
    }

    fun upgradeTo(tower: Tower, engine: Engine, entity: Entity, region: TextureRegion) {
        val transform = entity[TransformComponent.mapper].also {
            it ?: println("Upgrade to $tower failed \uD83D\uDCA5")
        } ?: return
        when (tower) {
            Tower.ATTACK_TOWER -> buildAttackTower(engine, transform.bounds, region)
            Tower.BUILDING_BLOCK -> println("Upgrading building block to... building block?")
        }
        engine.removeEntity(entity)
    }
}
