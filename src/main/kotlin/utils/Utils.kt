package utils

import Config.TILE_SIZE
import Coordinates
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Coordinates.toVector(): Vector2 {
    return Vector2(((x + 1) * TILE_SIZE), (y + 1) * TILE_SIZE)
}

fun Coordinates.adjacentCoordinates(): List<Coordinates> {
    return listOf(
        Coordinates(this.x, this.y + 1),
        Coordinates(this.x - 1, this.y),
        Coordinates(this.x + 1, this.y),
        Coordinates(this.x, this.y - 1),
    )
}

fun Vector2.toCoordinate(): Coordinates {
    return Coordinates(
        (x / TILE_SIZE).toInt() - 1,
        (y / TILE_SIZE).toInt() - 1
    )
}

fun Vector3.toCoordinate(): Coordinates {
    return Coordinates(
        (x / TILE_SIZE).toInt() - 1,
        (y / TILE_SIZE).toInt() - 1
    )
}

fun Rectangle.toCoordinate(): Coordinates {
    return Coordinates(
        (x / TILE_SIZE).toInt() - 1,
        (y / TILE_SIZE).toInt() - 1
    )
}

fun Rectangle.getCenterXY(): Vector2 {
    return Vector2(x + width / 2f, y + height / 2f)
}
