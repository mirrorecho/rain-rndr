package rain.rndr

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.openrndr.Program
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import rain.interfaces.*
import rain.language.*
import rain.machines.Machine
import rain.patterns.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlin.time.Duration

abstract class RndrMachine(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) {

    open class MachineInstance(
        val machine: RndrMachine,
        val program: Program, // TODO: needed?
        val properties: Map<String, Any>,
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

    abstract fun instanceFactory(machine: RndrMachine=this, program: Program, properties: Map<String, Any>): MachineInstance

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

    fun triggerOn(runningTime:Double, program: Program, properties: Map<String, Any>): MachineInstance =
        instanceFactory(this, program,
            this.properties.toMutableMap().apply {putAll(properties)}).start().apply { instances.add(this) }

    // TODO: should this immediately kill the instance? (current implementation) Or trigger a process to eventually kill?
    fun triggerOff(instance: MachineInstance) {
        instances.remove(instance.stop())
    }

}

// ===========================================================================================================

open class Circle(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context: ContextInterface = LocalContext,
): RndrMachine(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Circle> = Label(
            factory = { k, p, c -> Circle(k, p, c) },
            labels = listOf("Circle", "RndrMachine", "Machine"),
        )
    }

    override val label: LabelInterface get() = Circle.label

    class MachineInstance(
        machine: RndrMachine,
        program: Program, // TODO: needed?
        properties: Map<String, Any>,
    ): RndrMachine.MachineInstance(machine, program, properties) {

        // NOTE this doesn't seem to be able to work given class hierarchy / context approach
        // TODO maybe: explore more?
//        val animation = object : Animatable() {
//            var x = 0.0
//            var y = 0.0
//        }
        class AnimatedCircle : Animatable() {
            var x: Double = 0.0
        }

        val animatedCircle = AnimatedCircle()

        val hHSV = Random.nextDouble(0.2, 0.4)
        val sHSV = Random.nextDouble(0.2, 0.8)
        val vHSV get() = this.properties["vHSV"] as Double
        val radius get() = this.properties["radius"] as Double


        override fun render() {
            this.program.apply {
                animatedCircle.updateAnimation()
                if (!animatedCircle.hasAnimations()) {
                    animatedCircle.apply {
                        ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
                        ::x.complete()
                        ::x.animate(0.0, 1000, Easing.CubicInOut)
                        ::x.complete()
                    }
                }
                drawer.fill = ColorHSVa(hHSV, sHSV, vHSV).toRGBa()
                drawer.stroke = null
                drawer.circle(animatedCircle.x, height / 2.0, radius)
            }
        }
    }

    override fun instanceFactory(machine: RndrMachine, program: Program, properties: Map<String, Any>): MachineInstance {
        return MachineInstance(machine, program, properties)
    }



}