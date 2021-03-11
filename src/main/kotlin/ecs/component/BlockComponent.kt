package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class BlockComponent : Component {

    companion object {
        val mapper = mapperFor<BlockComponent>()
    }


}
