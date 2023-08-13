package rain.sandbox

import org.openrndr.Program
import rain.language.Palette
import rain.machines.Machine
import rain.rndr.*
import rain.utils.autoKey

// TODO / NOTES: base machine types:
//  - AnimValue - a simple value that can be animated, with optional easing/envelope
//  - AnimMulti - multiple values, which can either be fixed values, animated values (with optional easing/envelope),
//      or references to other AnimValue or AnimMulti machines

fun triggerArg(argOpName: String) {}

fun triggerArg(argValue: Double) {}

// TODO: how to associate dependant MachineFuncs to particular ops?

// TODO: should the triggers be something fancier than simple maps? - YES!
//  - define a trigger as a THING (Class/Oject)

// TODO maybe: consider whether a trigger would ever be reused...
//  that could be an interesting idea with creative possibilities...

open class OpMap() {

}

open class MachineFuncOp(
    val machineFunc:MachineFunc,
    val opKey:String = autoKey(),
    val program: Program,
    val properties: Map<String, Any?> = mapOf(), // TODO: TOO VAGUE?
) {
    var isRunning: Boolean = false
    val opMap: MutableMap<String, MachineFuncOp> = mutableMapOf()



    // TODO: does triggering make sense in this context?
//    fun trigger(properties: Map<String, Any?>) { throw NotImplementedError() }

    fun stop() { this.machineFunc.stopOp(this) }
}

//class Trigger(
//    val machineKey: String,
//    val opKey: String = autoKey(),
//    val dur: Double = 0.0, // TODO maybe: consider whether this could be based on some logic and not just a simple value!
////    val machinesToOps: Map<String, String> = mapOf(), // NO! KISS!
//    // ... i.e. a trigger must always only trigger a single op!!
//    val program: Program,
//    ) {
//
//    fun getProperties(): Map<String, Any> = mapOf() // TODO: add logic for getting map!
//
//    // TODO maybe: keep machinePalette (or even the machine) around?
//    fun getMachine(machinePalette: Palette<RndrMachine>): RndrMachine? = machinePalette[this.machineKey]
//
//    fun triggerMachine(machinePalette: Palette<RndrMachine>) {
//        val machine = getMachine(machinePalette)
//        if (machine ==null ) println("$machineKey not found! Could not trigger machine.")
//        else {
//            // TODO: implement the op triggering
//            machine.triggerOp( opKey, program, getProperties() )
//        }
//    }
//
//}


// TODO: aha!? is the Trigger the same as the Op?
class Trigger(
    // NOTE: in order to KISS, assuming Trigger properties cannot be changed
    // once trigger created (so this is a Map instead of a MutableMap)
    val properties: Map<String, Any?> = mapOf(),

    // NOTE: the key here is the relationship name, the value is either:
    // - Null: use same op name as this trigger's op
    // - any string value: use that op's name
    val relatedMachinesToOps: Map<String, String?> = mapOf()
) {
    val machine: String by this.properties
    val op: String by this.properties // TODO maybe: consider with op keys should be simple integer indices
    val dur: Double by this.properties



//    // TODO maybe: implement these
//    val time: Double by this.properties
//    val startAction: Int by this.properties // 0=nothing, 1=start, 2=free, 3="pause" (op is not running, but still exists)
//    // TODO consider whether endAction is best specified by the trigger implementation (as opposed to the machine)
//    val endAction: Int by this.properties // ditto values as startAction

    // TODO IMPORTANT! If a trigger is triggering a new op, then it MUST be able to specify
    //  what dependent/related machine ops to use
    //  - default/blank ... create or reuse an op with the same name is this op



    // TODO maybe: keep machinePalette (or even the machine) around?
    fun getMachine(machinePalette: Palette<RndrMachine>): RndrMachine? = machinePalette[this.machine]

    fun triggerMachine(machinePalette: Palette<RndrMachine>) {
        val machineNode = getMachine(machinePalette)
        if (machine ==null ) println("$machine not found! Could not trigger machine.")
        else {
            // TODO: implement the op triggering
            machineNode.triggerOp( opKey, program, getProperties() )
        }
    }


}




val testProgram = Program(true)

val triggersToPlay2 = mutableMapOf(
    0.0 to listOf(
        Trigger("OPERATE", "SIZE_OP", 4.0, program=testProgram)
    ),
    1.0 to listOf(

    ),
)

val triggersToPlay1 = mutableMapOf(
    0.0 to listOf(
        Trigger(mapOf(
            "machine" to "CIRCLE1", // assume only 1 machine necessary for any given work?
            "op" to null, // op name will be auto-generated key
            "dur" to 4.0, // assume optional? (time could also just be when all associated machine durs are over?)
            // TODO maybe: implement these:
            "time" to 0.0,
            "startAction" to 1, // 0=nothing, 1=start, 2=free, 3="pause" (op is not running, but still exists)
            // TODO consider whether endAction is best specified by the machine implementation (as opposed to the trigger)
            "endAction" to 0, // ditto values as startAction
            ),
            mapOf(
                // omitting RADIUS because assuming in this example that Circle1 does not have a network-connected radius
                "POSITION" to null, // use trigger's "op" value
            )
        ),
        mapOf(
            "machine" to "OPERATE", // assume only 1 machine necessary for any given work?
            "key" to "SIZE_OP",
            "dur" to 4.0, // assume optional? (time could also just be when all associated machine durs are over?)
            "startAction" to 1, // 0=nothing, 1=start, 2=free, 3="pause" (op is not running, but still exists)
            // TODO consider whether endAction is best specified by the machine implementation (as opposed to the trigger)
            "endAction" to 0, // ditto values as startAction
        ),
        mapOf(
            "machine" to "OPERATE", // assume only 1 machine necessary for any given work?
            "key" to "SMALL_CIRCLE_OP",
            "dur" to 4.0, // assume optional? (time could also just be when all associated machine durs are over?)
            "startAction" to 1, // 0=nothing, 1=start, 2=free, 3="pause" (op is not running, but still exists)
            // TODO consider whether endAction is best specified by the machine implementation (as opposed to the trigger)
            "endAction" to 0, // ditto values as startAction
        ),
        mapOf(
            // MACHINE: kick off SIZE ... getting bigger for 2"
            "machine" to "SIZE", // (an AnimValue)
            "startValue" to 2.0,
            "endValue" to 40.0,
            "dur" to 2.0,
            "op" to "SIZE_OP",
        ),
        mapOf(
            "machine" to "SMALL_CIRCLE", // (a Circle, which inherits from AnimMulti)

            // "op" to null, // NOTE: null (the default) means auto-create a new op

            // TODO: how to kick off (or connect to) an op for the circle?

            // NOTE that the Circle machine must define these as being able to be directly triggered through the Circle
            // (as opposed to separately triggering a Color machine)
            "stroke_h" to triggerArg(290.0),
            "stroke_s" to triggerArg(0.9),
            "stroke_v" to triggerArg(0.8),
            "stroke_a" to triggerArg(1.0),

            "radius" to triggerArg("SIZE_OP"), // TODO: how to connect this to the SIZE machine/op above (shared among many circles)?

            // need to deal with:
            // - radius
            // - POSITION - x
            // - POSITION - y
            // - STROKE_COLOR - h
            // - STROKE_COLOR - s
            // - STROKE_COLOR - v
            // - STROKE_COLOR - a
            // - stroke_weight
            // - FILL_COLOR - h
            // - FILL_COLOR - s
            // - FILL_COLOR - v
            // - FILL_COLOR - a

            // "ops" to listOf("SIZE_OP", "SMALL_CIRCLE_OP"),
            // TODO: OK, but how to handle the case where multiple ops could would have values
            //  with the same names? (e.g. values for HSVa for fill vs stroke)

        ),
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "op" to 1003,
            // "HEIGHT" to 1003, // NOTE... this is redundant / assumed
            "gate" to true,
            "dur" to 4.0,
            "doneAction" to 2,
        ),
        mapOf(
            "machine" to "SIZE",
            "op" to 1002,
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
            // 0=nothing, 1="pause" (op is not running, but still exists), 2=free
            "doneAction" to 2,
        ),
    ),
    1.0 to listOf(
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
        ),
    ),
    1.4 to listOf(
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
        ),
    ),
    2.0 to listOf(
        mapOf(
            // now SIZE gets slightly smaller for the 2nd 2"
            "machine" to "SIZE",
            "startValue" to 40.0, // redundant?
            "endValue" to 20.0,

            // this is the operation of the machine
            "op" to 1001,
            "dur" to 2.0,
            //"startAction" to 0 // not needed, since 0 is the default
            "endAction" to 2 // free
        ),
    ),
    4.0 to listOf(
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
        ),
    ),
)