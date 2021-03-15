package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class EnemyComponent : Component {
    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }

    val maxHealth = 130f
    var health = maxHealth

}
