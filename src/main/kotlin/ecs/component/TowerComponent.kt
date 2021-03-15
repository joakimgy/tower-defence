package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class TowerComponent : Component {
    companion object {
        val mapper = mapperFor<TowerComponent>()
    }

    val range = 100f
    val damage = 10f

}