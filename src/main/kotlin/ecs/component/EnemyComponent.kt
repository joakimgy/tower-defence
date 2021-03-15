package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor
import utils.Coordinate

class EnemyComponent : Component {
    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }

    var speed = 300f

    var path: MutableList<Coordinate> = mutableListOf()

}
