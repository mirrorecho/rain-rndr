package rain.rndr

import org.openrndr.Program
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import rain.interfaces.ContextInterface
import rain.interfaces.LabelInterface
import rain.interfaces.SelectDirection
import rain.language.ItemCompanion
import rain.language.Label
import rain.language.LocalContext
import rain.language.Relationship
import rain.machines.Machine
import rain.patterns.Leaf
import rain.patterns.Pattern


// TODO: consider whether these should be nodes in the graph (assume NOT, as in below)
open class MachineFuncOp(
    val machineKey:String,
    val opKey:String = rain.utils.autoKey(),
    val program: Program,
    val properties: Map<String, Any?> = mapOf(), // TODO: TOO VAGUE?
) {
    var isRunning: Boolean = false
    fun trigger(properties: Map<String, Any?>) { throw NotImplementedError() }
}

// TODO: or is this an interface?
// TODO: can MachineFuncs have multiple instances? (assume YES)
// MachineFunc is a factory for MachineFuncOp ...
open class MachineFunc(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) { // TODO: is Leaf the best parent class?

    // TODO: consider whether to implement property accessor and/or update/animate methods via lambda or inheritance
    var updateOpFunc: (op: MachineFuncOp)-> Unit = {}

    fun updateOps() {
        ops.values.forEach { this.updateOpFunc(it) }
    }

    val ops = mutableMapOf<String, MachineFuncOp>()

    // TODO: create an interface for accessing any running Op
    // ... is below sufficient?

    fun opProperty(opKey: String, name:String) = ops[opKey]!!.properties[name]

    fun <T>opPropertyAs(opKey: String, name:String) = ops[opKey]!!.properties[name] as T

    fun opVal(opKey: String): Double {
        return this.opPropertyAs(opKey, "VAL")
    }

    // TODO: can we avoid having to pass runningTime as in below?
//    fun trigger(properties: Map<String, Any?>) { throw NotImplementedError() }
    override fun trigger(runningTime:Double, properties: Map<String, Any?>) { throw NotImplementedError() }

    // TODO: ops management with dur (i.e. kill op when dur is up)

    // TODO: helper method(s) to create this along with necessary relationships/nodes
    // (and do the same for specifics below)

}

// TODO: plan for connecting MachineFuncs to Machine via relationships
abstract class RndrMachine3(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): MachineFunc(key, properties, context) {

    abstract fun renderOp(op:MachineFuncOp)

    fun render() {
        ops.values.forEach {
            this.updateOpFunc(it)
            this.renderOp(it)
        }
    }

}

open class Position(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): MachineFunc(key, properties, context) {
    val x: MachineFunc by lazy { targetsAs("POSITION_X") }
    val y: MachineFunc by lazy { targetsAs("POSITION_Y") }

    fun setTargets(x:MachineFunc?, y:MachineFunc?) {
        if (x == null) {
            val xFunc = MachineFunc()
            // TODO, should be specific type(label) of relationship for POSITION_X
            Relationship(source_key = this.key, target_key = xFunc.key).createMe()
        } else {}
    }

    fun vector(opKey:String): Vector2 = Vector2(x.opVal(opKey), y.opVal(opKey))
}

open class Color(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): MachineFunc(key, properties, context) {
    val h: MachineFunc by lazy { targetsAs("COLOR_H") }
    val s: MachineFunc by lazy { targetsAs("COLOR_S") }
    val v: MachineFunc by lazy { targetsAs("COLOR_V") }
    val a: MachineFunc by lazy { targetsAs("COLOR_A") }

    fun colorRGBa(opKey:String): ColorRGBa = ColorHSVa(
        h.opVal(opKey), s.opVal(opKey), v.opVal(opKey), a.opVal(opKey)
    ).toRGBa()

}

open class Circle3(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): RndrMachine3(key, properties, context) {

    val radius: MachineFunc by lazy { targetsAs("RADIUS") }
    val position: Position by lazy { targetsAs("POSITION") }
    val strokeColor: Color? by lazy { targetsAs("STROKE_COLOR") }
    val strokeWeight: MachineFunc by lazy { targetsAs("STROKE_WEIGHT") }
    val fillColor: Color? by lazy { targetsAs("FILL_COLOR") }

    override fun renderOp(op:MachineFuncOp) {
            op.program.apply {
                // TODO: pass separate keys for each op
                drawer.stroke = strokeColor?.colorRGBa(op.opKey)
                drawer.strokeWeight = strokeWeight.opVal(op.opKey)
                drawer.fill = fillColor?.colorRGBa(op.opKey)
                drawer.circle(
                    position.vector(op.opKey),
                    radius.opVal(op.opKey)
                )
        }
    }

}

//
//
//// testing this out to see how inheritance might play out...
//open class Circle2(
//    key:String = rain.utils.autoKey(),
//    properties: Map<String, Any?> = mapOf(),
//    context: ContextInterface = LocalContext,
//): rain.machines.Printer(key, properties, context) { // NOTE: just using Printer for simplicity sake...
//
//    // TODO: IDEALLY - REMOVE
////    companion object : ItemCompanion() {
////        override val label: Label<Circle2> = Label(
////            factory = { k, p, c -> Circle2(k, p, c) },
////            labels = listOf("Circle2", "RndrShape", "RndrMachine", "Machine"),
////        )
////    }
//
//    // TODO: IDEALLY - REMOVE
////    override val label: LabelInterface get() = Circle2.label
//
//    // TODO: IDEALLY - REMOVE
////    override fun opFactory(machine: RndrMachine, program: Program, properties: MutableMap<String, Any?>): RndrOp {
////        return CircleOp(machine as Circle, program, properties.toMutableMap())
////    }
//
//    // these properties would apply be for any shapes:
//    var x: Double by this.properties.apply { putIfAbsent("x", 200.0) }
//    var y: Double by this.properties.apply { putIfAbsent("y", 200.0) }
//
//    var radius: Double by this.properties.apply { putIfAbsent("radius", 90.0) }
//
//    fun render(op: OpInterface) {
//        super.render()
//
//        op.program.apply {
////            drawer.fill = fill
////            drawer.stroke = stroke
////            drawer.strokeWeight = strokeWeight
//            // TODO: are these defaults OK?
//            drawer.circle(x * op.program.width, y * op.program.height, radius)
//        }
//    }
//
//    class CircleOp(
//        override val machine: Circle,
//        program: Program,
//        properties: MutableMap<String, Any?>,
//    ): RndrAnimationOp(machine, program, properties) {
//
//        var radius: Double = 0.0
//
//        init {
//            animatableMap.putAll(mapOf(
//                "radius" to ::radius,
//            ))
//        }
//
//        override fun render() {
//            super.render()
//
//            this.program.apply {
//                drawer.fill = fill
//                drawer.stroke = stroke
//                drawer.strokeWeight = strokeWeight
//                // TODO: are these defaults OK?
//                drawer.circle(x * program.width, y * program.height, radius)
//            }
//        }
//    }
//}