package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PlayerComponent : Component {

    val maxHealth = 100f
    var health = maxHealth

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

}
