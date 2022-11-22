package rain.rndr

import com.sun.org.apache.xpath.internal.operations.Bool
import org.openrndr.Program
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorHSVa
import rain.interfaces.*
import rain.language.*
import rain.machines.Machine
import rain.patterns.*
import kotlin.random.Random

// represents an instance of a machine being operated
interface RndrOp {
    val machine: RndrMachine
    var running: Boolean
    val dur:Double // TODO: consider accommodating ops with indeterminate durs...
    val program: Program // TODO: needed?

    val op: String

    fun start(): RndrOp {
        running = true
        return this
    }

    fun render() {}

    fun stop(): RndrOp {
        running = false
        machine.cleanup(this)
        return this
    }
}


// think of RndrMachine kind of like an sc SynthDef ... it's a blueprint,
// that also implements the rain Machine interface... triggering the machine
// creates a new instance of a running machine instance

abstract class RndrMachine(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) {



    // TODO: naming? (since "Instance" has it's own OOP meaning)
//    open class MachineInstance(
//        open val machine: RndrMachine,
//        val program: Program, // TODO: needed?
//        val properties: MutableMap<String, Any?>,
//    ) {
//
//        var running = false
//
//        val dur:Double get() = this.properties["dur"] as Double
//
//        open fun render() {}
//
//        fun start(): MachineInstance {
//            running = true
//            return this
//        }
//
//        fun stop(): MachineInstance {
//            running = false
//            machine.cleanup(this)
//            return this
//        }
//    }

    abstract val poly: Boolean

    abstract fun opFactory(machine: RndrMachine=this, program: Program, properties: MutableMap<String, Any?>): RndrOp

    val ops = mutableMapOf<String, RndrOp>()

    override fun render() {
        ops.forEach {
            if (it.value.running) it.value.render() }
    }

    override fun reset() {
        // TODO: implement
        throw NotImplementedError()
    }

//    abstract fun renderInstance(instance: MachineInstance)

    fun cleanup(op: RndrOp) {}

    fun triggerOn(runningTime:Double, program: Program, properties: Map<String, Any?>): RndrOp =
        opFactory(this, program,
            this.properties.toMutableMap().apply {putAll(properties)}).start().apply { ops.add(this) }

    // TODO: implement trigger, which can either triggerOn, triggerOff, or update

    // TODO: should this immediately kill the instance? (current implementation) Or trigger a process to eventually kill?
    fun triggerOff(op: RndrOp) {
        ops.remove(op.stop().op)
    }

}
