package ecs.component

import Config
import Coordinates
import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class EnemyComponent : Component {
    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }

    var speed = Config.TILE_SIZE * 2f

    var path: MutableList<Coordinates> = mutableListOf()
}
