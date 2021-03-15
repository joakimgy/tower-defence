package ecs.component

import MAP_SIZE_X
import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor
import utils.Coordinate

class EnemyComponent : Component {
    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }

    val maxHealth = 130f
    var health = maxHealth
    var speed = 100f

    var path = mutableListOf(
        Coordinate(1, 5),
        Coordinate(10, 5),
        Coordinate(10, 12),
        Coordinate(15, 12),
        Coordinate(MAP_SIZE_X, 0),
    )

}
