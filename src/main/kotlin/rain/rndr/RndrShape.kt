package rain.rndr

import org.openrndr.Program
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import rain.interfaces.ContextInterface
import rain.interfaces.LabelInterface
import rain.language.ItemCompanion
import rain.language.Label
import rain.language.LocalContext
import kotlin.random.Random

interface ShapeInterfcae {

    // any shape class (whether a machine, machine instance, or instance animation
    // would implement this interface with these properties...

    var fillH: Double?
    var fillS: Double?
    var fillV: Double?
    var fillA: Double?

    var strokeH: Double?
    var strokeS: Double?
    var strokeV: Double?
    var strokeA: Double?

    var x: Double?
    var y: Double?

    val position: Vector2 get() = Vector2(x!!, y!!)

    val fill: ColorRGBa? get() {
        Easing.None
        if (fillH!= null || fillV!= null) {
            return ColorHSVa(fillH ?: 0.0, fillS ?: 0.9, fillV ?: 0.9, fillA ?: 1.0).toRGBa()
        } else return null
    }

    val stroke: ColorRGBa? get() {
        if (strokeH!= null || strokeV!= null) {
            return ColorHSVa(strokeH ?: 0.0, strokeS ?: 0.9, strokeV ?: 0.9, strokeA ?: 1.0).toRGBa()
        } else return null
    }

}

abstract class RndrShape (
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): RndrMachine(key, properties, context), ShapeInterfcae {

    // any shape machine node in the tree of possible machines
    // each object being equivalent to a "SynthDef" in sc and can be triggered
    // (with the class itself being a way to create
    // a bunch of machines (defs)

    // below are the values for the machine blueprint
    // ... effectively defaults for any machine instance created with this blueprint

    override var fillH: Double? by this.properties
    override var fillS: Double? by this.properties
    override var fillV: Double? by this.properties
    override var fillA: Double? by this.properties

    override var strokeH: Double? by this.properties
    override var strokeS: Double? by this.properties
    override var strokeV: Double? by this.properties
    override var strokeA: Double? by this.properties

    override var x: Double? by this.properties
    override var y: Double? by this.properties

    // TODO: standard logic to trigger new animation with existing machine instance

    open class MachineInstance(
        override val machine: RndrShape,
        program: Program,
        properties: MutableMap<String, Any?>,
    ): RndrMachine.MachineInstance(machine, program, properties), ShapeInterfcae {



        override var fillH: Double? by this.properties
        override var fillS: Double? by this.properties
        override var fillV: Double? by this.properties
        override var fillA: Double? by this.properties

        override var strokeH: Double? by this.properties
        override var strokeS: Double? by this.properties
        override var strokeV: Double? by this.properties
        override var strokeA: Double? by this.properties

        override var x: Double? by this.properties
        override var y: Double? by this.properties

        open class InstanceAnimation : Animatable(), ShapeInterfcae {

            // these are the values that actually change over time
            // NOTE: cannot use ? types here (don't work with animation interface)

            override var fillH: Double = null
            override var fillS: Double = null
            override var fillV: Double = null
            override var fillA: Double = null

            override var strokeH: Double = null
            override var strokeS: Double = null
            override var strokeV: Double = null
            override var strokeA: Double = null

            override var x: Double = null
            override var y: Double = null
        }

        open val animation = InstanceAnimation()


        open fun animate() {
            this.program.apply {
                animation.updateAnimation()
                if (!animation.hasAnimations()) {
                    animation.apply {
                        ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
                        ::x.complete()
                        ::x.animate(0.0, 1000, Easing.CubicInOut)
                        ::x.complete()
                    }
                }
            }
        }

    }

}


// ===========================================================================================================

open class Circle(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): RndrShape(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Circle> = Label(
            factory = { k, p, c -> Circle(k, p, c) },
            labels = listOf("Circle", "RndrShape", "RndrMachine", "Machine"),
        )
    }

    override val label: LabelInterface get() = Circle.label

    override fun instanceFactory(machine: RndrMachine, program: Program, properties: MutableMap<String, Any?>): MachineInstance {
        return MachineInstance(machine as Circle, program, properties)
    }

    var radius: Double by this.properties.apply { putIfAbsent("radius", 200.0) }

    class MachineInstance(
        override val machine: Circle,
        program: Program,
        properties: MutableMap<String, Any?>,
    ): RndrShape.MachineInstance(machine, program, properties) {

        var radius: Double by this.properties

        class CicleAnimation : RndrShape.MachineInstance.InstanceAnimation() {
            var radius: Double? = null
        }

        override val animation = CicleAnimation()

        override fun render() {
            super.render()

            this.program.apply {
                animation.updateAnimation()
                if (!animation.hasAnimations()) {
                    animation.apply {
                        ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
                        ::x.complete()
                        ::x.animate(0.0, 1000, Easing.CubicInOut)
                        ::x.complete()
                    }
                }
                drawer.fill = fill
                drawer.stroke = stroke
                // TODO: are these defaults OK?
                drawer.circle(animation.x ?: 0.0, animation.y ?: 0.0, animation.radius ?: 90.0)
            }
        }
    }





}