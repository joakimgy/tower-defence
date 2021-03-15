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
