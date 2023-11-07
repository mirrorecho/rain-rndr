package rain.rndr

import rain.utils.*
import org.openrndr.Program

// represents an action of a machine!
// created at the time of score construction!
// TODO: is this interface even worth it??
interface MachineAction {
    val name: String
    val properties: Map<String, Any?>
}


// DECISION 1: these are NOT nodes in the graph (should be arbitrarily able to spin up 1,000s of actions
// whenever, without modifying the underlying graph)
// DECISION 2: Acts implement both triggering and operation (combines previous attempts at
// creating a system for triggering in the score, and then separate operations of machines in
// some kind of data structure)
// TODO: confirm that the implementation below could be "animatoable" with an arbitrary # of
//  Double-type properties. If not, and the animatlable property needs to specifically defined as an
//  attribute on the class, then will need to rethink the implementation (probably so that an Op,
//  if animatable, only includes a single Double value that is animated).

// TODO maybe: consider whether a trigger would ever be reused...
//  that could be an interesting idea with creative possibilities...
open class Act(

    override val name: String = autoKey(),
    open val rndrMachine: RndrMachine<*>,

    // this is mutable because often an Act will need to be created in order to relate to,
    // and then updated later to add data
    override val properties: MutableMap<String, Any?> = mutableMapOf(),
    val score: Score,

    // TODO maybe: include context? Would it be used?

    // the key is the relationship name, the value is the Act object to use for that related machine
    // this is mutable for the same reason as properties above
    // ... TODO: implement logic to create this in score creation ... expect this to get NASTY!
    val relatedMachinesToActs: MutableMap<String, Act> = mutableMapOf()

): MachineAction {
    // val machine: String by this.properties // redundant since the MachineFunc object will need to be specified at the time of instantiation

    val dur: Double by this.properties

    val program: Program = score.program

    var isRunning: Boolean = false


//    // TODO maybe: implement these
//    val time: Double by this.properties
//    val startAction: Int by this.properties // 0=nothing, 1=start, 2=free, 3="pause" (op is not running, but still exists)
//    // TODO consider whether endAction is best specified by the trigger implementation (as opposed to the machine)
//    val endAction: Int by this.properties // ditto values as startAction

//    fun start() { this.machineFunc.startAct(this)  }

//    fun stop() { this.machineFunc.stopAct(this) } // assume not needed

    fun relatedAct(relationshipName:String): Act = relatedMachinesToActs[relationshipName]!!

    fun relatedVal(relationshipName:String): Double = relatedAct(relationshipName).properties["value"] as Double

    // TODO this logic should be moved to the score creation (may get nasty)
//    fun getMachine(machinePalette: Palette<RndrMachine>): RndrMachine? = machinePalette[this.machine]

    // TODO: implement this in score creation (may get nasty)
//    fun triggerMachine(machinePalette: Palette<RndrMachine>) {
//        val machineNode = getMachine(machinePalette)
//        if (machine ==null ) println("$machine not found! Could not trigger machine.")
//        else {
//            // TODO: implement the op triggering
//            machineNode.triggerOp( opKey, program, getProperties() )
//        }
//    }
}

//class Update(
//
//    // NOTE: in order to KISS, assuming Act properties cannot be changed
//    // once act created (so this is a Map instead of a MutableMap)
//    override val name: String = autoKey(), // TODO: maybe Update doesn't need to be named?
//    val machineFunc:MachineFunc, // TODO: is this really necessary on an udpate?
//    override val properties: Map<String, Any?> = mapOf(),
//
//): MachineAction {
//}