package ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import ktx.ashley.mapperFor

class TransformComponent() : Component {
    companion object {
        val mapper = mapperFor<TransformComponent>()
    }

    val bounds = Rectangle()

}

fun Rectangle.getCenterXY(): Vector2 {
    return Vector2(x + width / 2f, y + height / 2f)
}

