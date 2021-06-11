package ecs.component.buildings

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class AttackTowerComponent : Component {
    companion object {
        val mapper = mapperFor<AttackTowerComponent>()
    }

    val range = Config.AttackTower.range
    val damage = Config.AttackTower.damage
}

