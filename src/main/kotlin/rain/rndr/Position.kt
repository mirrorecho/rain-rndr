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
    // TODO: accommodate local storage
    val x: ValueFunc by lazy { targetsAs("POSITION_X") }
    val y: ValueFunc by lazy { targetsAs("POSITION_Y") }

    override val label = LocalContext.getLabel("Position", "MachineFunc", "Machine", "Leaf") { k, p, c -> Position(k, p, c) }

    // TODO: what was the idea of the below method?
//    fun setTargets(x:MachineFunc?, y:MachineFunc?) {
//        if (x == null) {
//            val xFunc = MachineFunc()
//            // TODO, should be specific type(label) of relationship for POSITION_X
//            Relationship(source_key = this.key, target_key = xFunc.key).createMe()
//        } else {}
//    }

    // TODO: accommodate local storage
    fun vector(act:Act): Vector2 = Vector2(
        act.relatedVal("POSITION_X"),
        act.relatedVal("POSITION_Y"),
    )
}