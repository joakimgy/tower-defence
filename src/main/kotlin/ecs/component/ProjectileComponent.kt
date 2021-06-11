package ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class ProjectileComponent() : Component {

    companion object {
        val mapper = mapperFor<ProjectileComponent>()
    }

    lateinit var targetEntity: Entity
    var damage: Float = 0.0f
}
