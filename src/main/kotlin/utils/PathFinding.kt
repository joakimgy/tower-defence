import utils.AlgorithmAStar
import utils.Graph
import kotlin.math.abs
import kotlin.math.sqrt

data class Coordinates(
    val x: Int,
    val y: Int
) : Graph.Vertex

data class Route(
    override val a: Coordinates,
    override val b: Coordinates
) : Graph.Edge<Coordinates> {
    val distance: Double
        get() {
            val x = abs(a.y - b.y)
            val y = abs(a.x - b.x)
            return sqrt((x * x + y * y).toDouble())
        }
}

class AlgorithmAStarImpl(edges: List<Route>) : AlgorithmAStar<Coordinates, Route>(edges) {
    override fun costToMoveThrough(edge: Route): Double {
        return edge.distance
    }

    override fun createEdge(from: Coordinates, to: Coordinates): Route {
        return Route(from, to)
    }
}

fun main() {
    val routes = listOf(
        Route(Coordinates(1, 1), Coordinates(1, 3)),
        Route(Coordinates(1, 1), Coordinates(5, 1)),
        Route(Coordinates(5, 1), Coordinates(2, 2)),
        Route(Coordinates(1, 3), Coordinates(2, 2))
    )

    val result = AlgorithmAStarImpl(routes)
        .findPath(
            begin = Coordinates(1, 1),
            end = Coordinates(2, 2)
        )

    val pathString = result.first.joinToString(separator = ", ") { "[${it.x}, ${it.y}]" }

    println("Result:")
    println("  Path: $pathString")
    println("  Cost: ${result.second}")
}