package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class HealthComponent : Component {

    var maxHealth = Config.Player.health
    var health = maxHealth

    companion object {
        val mapper = mapperFor<HealthComponent>()
    }
}
