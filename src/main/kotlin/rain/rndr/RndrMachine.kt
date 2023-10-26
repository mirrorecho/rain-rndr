package rain.rndr

import org.openrndr.Program
import rain.interfaces.ContextInterface
import rain.language.LocalContext



// TODO: combine Machine and MachineFunc?
// TODO: plan for connecting MachineFuncs to Machine via relationships
// TODO: maybe this class should be abstract?
// TODO: does this class have any purpose at all anymore now that renderOp also implemented on machineFunc?
open class RndrMachine(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): MachineFunc(key, properties, context) {

    override val label = LocalContext.getLabel("RndrMachine", "MachineFunc", "Machine", "Leaf") { k, p, c -> RndrMachine(k, p, c) }


//    fun render() {
//        ops.values.forEach {
//            this.updateOp(it)
//            this.renderOp(it)
//        }
//    }

}