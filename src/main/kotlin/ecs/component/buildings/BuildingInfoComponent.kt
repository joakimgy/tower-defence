package ecs.component.buildings

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class BuildingInfoComponent : Component {
    companion object {
        val mapper = mapperFor<BuildingInfoComponent>()
    }

    lateinit var entity: Entity
}
