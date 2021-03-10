package ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PlayerComponent : Component {
    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

    val position = Position(60f, 20f)
}

data class Position(val x: Float, val y: Float)