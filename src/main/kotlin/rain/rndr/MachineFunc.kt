package rain.rndr

import rain.utils.*
import org.openrndr.Program
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import rain.interfaces.ContextInterface
import rain.language.LocalContext
import rain.language.Relationship
import rain.machines.Machine
import rain.patterns.Leaf

open class NodeOrPropertyValue(
    // represents a Double value that can be:
    // (1) get/set via this reference node's own properties
    // or (2) get/set via a relationship to another node
    // ... and for either (1) or (2) above, can either be:
    // (a) fixed
    // or (b) animated
    val referenceNode: rain.language.Node,
    val name:String,
    val targetRelationshipLabel: String? = null,
    var defaultValue: Double = 0.0,
) {
    val context = referenceNode.context

    fun getValue(opName: String? = null) {

    }
}


// DECISION: these are NOT nodes in the graph (should be arbitrarily able to spin up 1,000s of ops
// whenever, without modifying the underlying graph)
// TODO: confirm that the implementation below could be "animatoable" with an arbitrary # of
//  Double-type properties. If not, and the animatlable property needs to specifically defined as an
//  attribute on the class, then will need to rethink the implementation (probably so that an Op,
//  if animatable, only includes a single Double value that is aninated).
open class MachineFuncOp(
    val machineFunc:MachineFunc,
    val opKey:String = autoKey(),
    val program: Program,
    val properties: Map<String, Any?> = mapOf(), // TODO: TOO VAGUE?
) {
    var isRunning: Boolean = false

    // TODO: does trigerring make sense in this context?
//    fun trigger(properties: Map<String, Any?>) { throw NotImplementedError() }

    fun stop() { this.machineFunc.stopOp(this) }
}

// TODO: or is this an interface?
// TODO: can MachineFuncs have multiple instances? (assume YES)
// MachineFunc is a factory for MachineFuncOp ...
open class MachineFunc(
    key:String = autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) { // TODO: is Leaf the best parent class?

    override val label = LocalContext.getLabel("MachineFunc", "Machine", "Leaf") { k, p, c -> MachineFunc(k, p, c) }

    // TODO: consider whether to implement property accessor and/or update/animate methods via lambda or inheritance
    open var triggerOp: (String, Program, Map<String, Any?>)-> MachineFuncOp = { opKey, program, properties ->
        MachineFuncOp(this, opKey, program, mapCopy(this.properties, properties))
    }
    open var updateOp: (op: MachineFuncOp)-> Unit = {}
    open var renderOp: (MachineFuncOp)->Unit = {}
    open var stopOp: (op: MachineFuncOp)-> Unit = {}


    // TODO: create an interface for accessing any running Op
    // ... is below sufficient?

    fun opProperty(opKey: String, name:String) = context.getOp(this.key, opKey)!!.properties[name]

    fun <T>opPropertyAs(opKey: String, name:String) = context.getOp(this.key, opKey)?.properties?.get(name) as T

    // TODO: can we avoid having to pass runningTime as in below?
//    fun trigger(properties: Map<String, Any?>) { throw NotImplementedError() }
    fun trigger(runningTime:Double, program: Program, triggerProperties: Map<String, Any?>): MachineFuncOp {
        val opKey:String = triggerProperties["OP"] as String? ?: rain.utils.autoKey()
        val op = triggerOp(opKey, program, triggerProperties)
        op.isRunning = true
        return op
    }

    // should no longer be needed since all ops are now managed through the context
//    fun triggerOff(op: MachineFuncOp) {
//        stopOp(op)
//        op.isRunning = false
//        ops.remove(op.opKey)
//    }

    // TODO: ops management with dur (i.e. kill op when dur is up)

    // TODO: helper method(s) to create this along with necessary relationships/nodes
    // (and do the same for specifics below)

}

// TODO: ANIMATE THE OP!!!!!!
open class ValueFunc(
    key:String = autoKey(),
    value: Double = 0.0, // TODO maybe: consider whether type should be Double? with default null
    context: ContextInterface = LocalContext,
): MachineFunc(key, mapOf("value" to value), context) {
    override val label = LocalContext.getLabel("ValueFunc", "MachineFunc", "Machine", "Leaf") { k, p, c -> MachineFunc(k, p, c) }

    var value: Double by this.properties

    fun opVal(opKey: String): Double {
        return this.opPropertyAs(opKey, "value")
    }

}
