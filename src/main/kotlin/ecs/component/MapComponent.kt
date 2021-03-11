package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class MapComponent : Component {
    companion object {
        val mapper = mapperFor<MapComponent>()
    }


}

