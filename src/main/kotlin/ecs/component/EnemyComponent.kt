package ecs.component

import Coordinates
import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class EnemyComponent : Component {
    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }

    var speed = 300f

    var path: MutableList<Coordinates> = mutableListOf()

}
