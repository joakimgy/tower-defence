package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class EnemyComponent : Component {
    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }

    val health = 100f;
}
