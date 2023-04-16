package rain.rndr

import org.openrndr.math.Vector2
import rain.interfaces.ContextInterface
import rain.language.LocalContext
import rain.language.Relationship

open class Position(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): MachineFunc(key, properties, context) {
    val x: ValueFunc by lazy { targetsAs("POSITION_X") }
    val y: ValueFunc by lazy { targetsAs("POSITION_Y") }

    override val label = LocalContext.getLabel("Position", "MachineFunc", "Machine", "Leaf") { k, p, c -> Position(k, p, c) }

    fun setTargets(x:MachineFunc?, y:MachineFunc?) {
        if (x == null) {
            val xFunc = MachineFunc()
            // TODO, should be specific type(label) of relationship for POSITION_X
            Relationship(source_key = this.key, target_key = xFunc.key).createMe()
        } else {}
    }

    // TODO: these need to be separate keys!!!
    fun vector(opKey:String): Vector2 = Vector2(x.opVal(opKey), y.opVal(opKey))
}