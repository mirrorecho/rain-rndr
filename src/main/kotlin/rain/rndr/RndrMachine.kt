package rain.rndr

import rain.utils.*
import rain.interfaces.*
import rain.language.*
import  rain.machines.*
import rain.patterns.*

// OLD TODOS:
// TODO: combine Machine and MachineFunc?
// TODO: plan for connecting MachineFuncs to Machine via relationships
// TODO: maybe this class should be abstract?
// TODO: does this class have any purpose at all anymore now that renderOp also implemented on machineFunc?


// TODO: reconfigure so Act type param not needed at class level, only at fun level
open class RndrMachine(
    key:String = autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) { // TODO: is Leaf the best parent class? (Relationships might not be simple tree patterns.)

    override val label = LocalContext.getLabel("RndrMachine", "Machine", "Leaf") { k, p, c -> RndrMachine<T>(k, p, c) }

    open var createAct: <T>(score:Score, name:String, actProperties: Map<String, Any?>) -> T? = {
        s, n, p ->
        null
    }

    // TODO: is this even used?
    override fun trigger(runningTime:Double, properties: Map<String, Any?>) {
        println("$runningTime: " + this.key + " " + properties.toString())
    }

}