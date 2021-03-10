package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class MapComponent : Component {

    val blocks = HashMap<Position, Boolean>()

    companion object {
        val mapper = mapperFor<MapComponent>()
    }


}

data class Position(val x: Int, val y: Int);