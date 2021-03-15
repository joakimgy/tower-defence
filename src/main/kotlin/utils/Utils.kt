package utils

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2


fun Rectangle.getCenterXY(): Vector2 {
    return Vector2(x + width / 2f, y + height / 2f)
}

