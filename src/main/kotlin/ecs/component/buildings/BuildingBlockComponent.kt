package ecs.component.buildings

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class BuildingBlockComponent : Component {
    companion object {
        val mapper = mapperFor<BuildingBlockComponent>()
    }
}