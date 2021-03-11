package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class MapComponent : Component {

    val blocks = HashMap<Position, BlockComponent>()

    companion object {
        val mapper = mapperFor<MapComponent>()
    }


}

data class Position(var x: Int, var y: Int);