package rain.rndr

import org.openrndr.Program
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorHSVa
import rain.interfaces.*
import rain.language.*
import rain.machines.Machine
import rain.patterns.*
import kotlin.random.Random

abstract class RndrMachine(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) {

    open class MachineInstance(
        open val machine: RndrMachine,
        val program: Program, // TODO: needed?
        val properties: MutableMap<String, Any?>,
    ) {

        var running = false

        val dur:Double get() = this.properties["dur"] as Double

        open fun render() {}

        fun start(): MachineInstance {
            running = true
            return this
        }

        fun stop(): MachineInstance {
            running = false
            machine.cleanup(this)
            return this
        }

    }

    abstract fun instanceFactory(machine: RndrMachine=this, program: Program, properties: MutableMap<String, Any?>): MachineInstance

    val instances = mutableListOf<MachineInstance>()

    override fun render() {
        instances.forEach { if (it.running) it.render() }
    }

    override fun reset() {
        // TODO: implement
        throw NotImplementedError()
    }

//    abstract fun renderInstance(instance: MachineInstance)

    fun cleanup(instance: MachineInstance) {}

    fun triggerOn(runningTime:Double, program: Program, properties: Map<String, Any?>): MachineInstance =
        instanceFactory(this, program,
            this.properties.toMutableMap().apply {putAll(properties)}).start().apply { instances.add(this) }

    // TODO: implement trigger, which can either triggerOn, triggerOff, or update

    // TODO: should this immediately kill the instance? (current implementation) Or trigger a process to eventually kill?
    fun triggerOff(instance: MachineInstance) {
        instances.remove(instance.stop())
    }

}
