package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class HealthComponent : Component {

    var maxHealth = 130f
    var health = maxHealth

    companion object {
        val mapper = mapperFor<HealthComponent>()
    }

}