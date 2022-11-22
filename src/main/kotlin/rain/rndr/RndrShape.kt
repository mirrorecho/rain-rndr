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

interface ShapeInterface {

    // any shape class (whether a machine, machine instance, or instance animation
    // would implement this interface with these properties...


    var fillH: Double
    var fillS: Double
    var fillV: Double
    var fillA: Double

    var strokeH: Double
    var strokeS: Double
    var strokeV: Double
    var strokeA: Double

    var x: Double
    var y: Double

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
): RndrMachine(key, properties, context), ShapeInterface {

    // any shape machine node in the tree of possible machines
    // each object being equivalent to a "SynthDef" in sc and can be triggered
    // (with the class itself being a way to create
    // a bunch of machines (defs)

    // below are the values for the machine blueprint
    // ... effectively defaults for any machine instance created with this blueprint

    override val poly: Boolean by this.properties.apply { putIfAbsent("poly", true) } // TODO: implement

    override var fillH: Double by this.properties.apply { putIfAbsent("radius", 90.0) }
    override var fillS: Double by this.properties.apply { putIfAbsent("radius", 0.5) }
    override var fillV: Double by this.properties.apply { putIfAbsent("radius", 0.5) }
    override var fillA: Double by this.properties.apply { putIfAbsent("radius", 0.2) }

    override var strokeH: Double by this.properties.apply { putIfAbsent("radius", 90.0) }
    override var strokeS: Double by this.properties.apply { putIfAbsent("radius", 0.5) }
    override var strokeV: Double by this.properties.apply { putIfAbsent("radius", 0.5) }
    override var strokeA: Double by this.properties.apply { putIfAbsent("radius", 1.0) }

    override var x: Double by this.properties.apply { putIfAbsent("x", 0.5) }
    override var y: Double by this.properties.apply { putIfAbsent("v", 0.5) }

    // TODO: standard logic to trigger new animation with existing machine instance?
//    override fun opFactory(machine: RndrMachine=this, program: Program, properties: MutableMap<String, Any?>): RndrAnimation {
//        return ...
//    }

//    abstract val animation: RndrAnimation

    // TODO: assume OK to not do anything with animation here?
//    open fun animate() {
//        this.program.apply {
//            this@RndrShape.ops.forEach {it as RndrAnimation
//                if (it.running && it.hasAnimations()) { // TODO: is it even necessary to test for hasAnimations?
//                    it.animate()
//                }
//            }
//        }
//    }

}

open class RndrAnimationOp(
    override val machine: RndrShape,
    override val program: Program,
    val properties: MutableMap<String, Any?>, // TODO: reconsider using properties?

): RndrOp, ShapeInterface, Animatable() {
    // TODO: naming?
    var op: String by this.properties.apply { if (machine.poly) putIfAbsent("op", rain.utils.autoKey()) else machine.key }

    override var fillH: Double by this.properties
    override var fillS: Double by this.properties
    override var fillV: Double by this.properties
    override var fillA: Double by this.properties

    override var strokeH: Double by this.properties
    override var strokeS: Double by this.properties
    override var strokeV: Double by this.properties
    override var strokeA: Double by this.properties

    override var x: Double by this.properties // measured from 0 to 1 L to R
    override var y: Double by this.properties

    override var dur: Double by this.properties

    override var running: Boolean = true // or should this be false?

    override fun render() {
        super.render()
        this.updateAnimation()
        if (this.hasAnimations()) {
            this.apply {
//                ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
//                ::x.complete()
//                ::x.animate(0.0, 1000, Easing.CubicInOut)
//                ::x.complete()
            }
        } else {
            // TODO: check and set new animations here??
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

    override fun opFactory(machine: RndrMachine, program: Program, properties: MutableMap<String, Any?>): RndrOp {
        return CircleOp(machine as Circle, program, properties.toMutableMap())
    }

    var radius: Double by this.properties.apply { putIfAbsent("radius", 200.0) }


    class CircleOp(
        override val machine: Circle,
        program: Program,
        properties: MutableMap<String, Any?>,
    ): RndrAnimationOp(machine, program, properties) {

        var radius: Double by this.properties

        override fun render() {
            super.render()

            this.program.apply {
//                if (!animation.hasAnimations()) {
//                    animation.apply {
//                        ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
//                        ::x.complete()
//                        ::x.animate(0.0, 1000, Easing.CubicInOut)
//                        ::x.complete()
//                    }
//                }
                drawer.fill = fill
                drawer.stroke = stroke
                // TODO: are these defaults OK?
                drawer.circle(x, y, radius)
            }
        }
    }

}