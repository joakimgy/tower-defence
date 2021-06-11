package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class InteractableComponent : Component {
    companion object {
        val mapper = mapperFor<InteractableComponent>()
    }

    var cursorIsHovering = false
    var isClicked = false
}
