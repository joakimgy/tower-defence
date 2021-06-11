package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class InteractiveComponent : Component {
    companion object {
        val mapper = mapperFor<InteractiveComponent>()
    }

    var cursorIsHovering = false
    var isClicked = false
}
