package utils

import TILE_SIZE
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

data class Coordinate(val x: Int, val y: Int)

fun Coordinate.toVector(): Vector2 {
    return Vector2(((x + 1) * TILE_SIZE), (y + 1) * TILE_SIZE)
}

fun Vector2.toCoordinate(): Coordinate {
    return Coordinate(
        (x / TILE_SIZE).toInt() - 1,
        (y / TILE_SIZE).toInt() - 1
    )
}

fun Vector3.toCoordinate(): Coordinate {
    return Coordinate(
        (x / TILE_SIZE).toInt() - 1,
        (y / TILE_SIZE).toInt() - 1
    )
}

fun Rectangle.getCenterXY(): Vector2 {
    return Vector2(x + width / 2f, y + height / 2f)
}
