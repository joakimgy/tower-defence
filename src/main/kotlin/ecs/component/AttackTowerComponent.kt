import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class AttackTowerComponent : Component {
    companion object {
        val mapper = mapperFor<AttackTowerComponent>()
    }

    val range = 100f
    val damage = 20f
}