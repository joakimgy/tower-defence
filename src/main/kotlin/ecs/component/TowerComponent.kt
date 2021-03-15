package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

sealed class TowerComponent : Component

class AttackTowerComponent : TowerComponent() {
    companion object {
        val mapper = mapperFor<AttackTowerComponent>()
    }

    val range = 100f
    val damage = 20f
}

class BuildingBlockComponent : TowerComponent() {
    companion object {
        val mapper = mapperFor<BuildingBlockComponent>()
    }
}
