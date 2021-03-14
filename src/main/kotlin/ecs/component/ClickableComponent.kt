package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class ClickableComponent : Component {
    companion object {
        val mapper = mapperFor<ClickableComponent>()
    }

}