package rain.rndr

import org.openrndr.Program
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.LinearType
import org.openrndr.math.Vector2
import rain.interfaces.ContextInterface
import rain.interfaces.LabelInterface
import rain.language.ItemCompanion
import rain.language.Label
import rain.language.LocalContext
import kotlin.random.Random
import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

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

    val position: Vector2 get() = Vector2(x, y)

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

    override var fillH: Double by this.properties.apply { putIfAbsent("fillH", 90.0) }
    override var fillS: Double by this.properties.apply { putIfAbsent("fillS", 0.5) }
    override var fillV: Double by this.properties.apply { putIfAbsent("fillV", 0.5) }
    override var fillA: Double by this.properties.apply { putIfAbsent("fillA", 0.2) }

    override var strokeH: Double by this.properties.apply { putIfAbsent("strokeH", 90.0) }
    override var strokeS: Double by this.properties.apply { putIfAbsent("strokeS", 0.5) }
    override var strokeV: Double by this.properties.apply { putIfAbsent("strokeV", 0.5) }
    override var strokeA: Double by this.properties.apply { putIfAbsent("strokeA", 1.0) }

    override var x: Double by this.properties.apply { putIfAbsent("x", 0.5) }
    override var y: Double by this.properties.apply { putIfAbsent("y", 0.5) }

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

    override var name: String by this.properties.apply {
        // TODO: naming? and this is a nasty way to deal with poly...
//      putIfAbsent("name", if (machine.poly) rain.utils.autoKey() else this@RndrAnimationOp.machine.key)

        // TODO: ignoring poly for now
        putIfAbsent("name", rain.utils.autoKey() )
    }

    // NOTE... by this.properties doesn't seem to work here with the animation

    val animatableMap = mutableMapOf<String, KMutableProperty0<Double>>()

    override var fillH: Double = 0.0
    override var fillS: Double = 0.0
    override var fillV: Double = 1.0
    override var fillA: Double = 1.0

    override var strokeH: Double = 0.0
    override var strokeS: Double = 0.0
    override var strokeV: Double = 1.0
    override var strokeA: Double = 1.0

    override var x: Double = 0.0 // measured from 0 to 1 L to R
    override var y: Double = 0.0

    fun initAnimatedProperty(kProperty: KMutableProperty0<Double>) {
//        animatableMap[kProperty.name] = kProperty
//        return properties[kProperty.name] as Double? ?: 0.0
//        this.properties[kProperty.name]?.run { kProperty.animate(this as Double, 0.001.toLong()) }
        this.properties[kProperty.name]?.run { kProperty.set(this as Double) }
//        kProperty.

    }

    fun animatedDefault(key:String, fallbackDefault: Double=0.0) = properties[key] as Double? ?: fallbackDefault


    init {
        initAnimatedProperties(
            ::fillH,
            ::fillS,
            ::fillV,
            ::fillA,
            ::strokeH,
            ::strokeS,
            ::strokeV,
            ::strokeA,
            ::x,
            ::y,
        )
    }

    fun initAnimatedProperties(vararg kProperties: KMutableProperty0<Double>) {
        kProperties.forEach {
            animatableMap[it.name] = it
//            this.properties[it.name]?.run { it.animate(this as Double, 0.001.toLong()) }
        }
    }

    override var dur: Double by this.properties // TODO: this is pointless since the dur needs to come from the reTrigger argument

    override var running: Boolean = true // or should this be false?


    // TODO: add easing

    override fun reTrigger(properties: Map<String, Any?>) {
        println(this)
        println(this.machine.key + ", " + this.name + ": " + properties)
        val animProperties = properties.filter { it.key in animatableMap.keys}
//        animProperties.forEach {animatableMap[it.key]!!.cancel() }
        animProperties.forEach { animatableMap[it.key]!!.animate(it.value as Double, (properties["dur"] as Double * 1000.0).toLong()) }


//        properties.forEach {
//            if (it.key in animatableMap.keys) {
//                // TODO: this is a little wonky... able to simplify?
//                // .. see https://stackoverflow.com/questions/35525122/kotlin-data-class-how-to-read-the-value-of-property-if-i-dont-know-its-name-at
////                this::class.memberProperties
////                this.javaClass.kotlin.members.first { m-> m.name == it.key }
////                this.animate( this::class.members.first { m-> m.name == it.key }, it.value as Double, (dur * 1000.0).toLong()) // TODO: add easing
////                println(it.key  +": " + animatableMap[it.key]!!.name)
////                this.animate(animatableMap[it.key]!!, it.value as Double, (dur * 1000.0).toLong())
//                animatableMap[it.key]!!.animate(it.value as Double, (dur * 1000.0).toLong())
//                animatableMap[it.key]!!.complete()
//            }
//        }
    }

    var renderedOnce = false
    fun renderOnce() {
//        initAnimatedProperty(::x)
//        renderedOnce = true
    }

    override fun render() {
        if (!renderedOnce) renderOnce()
        super.render()
        this.updateAnimation()
//        if (this.hasAnimations()) {
//            this.apply {
////                ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
////                ::x.complete()
////                ::x.animate(0.0, 1000, Easing.CubicInOut)
////                ::x.complete()
//            }
//        } else {
//            // TODO: check and set new animations here??
//        }
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
        println("MAKING CIRCLE")
        return CircleOp(machine as Circle, program, properties.toMutableMap())
    }

    var radius: Double by this.properties.apply { putIfAbsent("radius", 200.0) }


    class CircleOp(
        override val machine: Circle,
        program: Program,
        properties: MutableMap<String, Any?>,
    ): RndrAnimationOp(machine, program, properties) {

        var radius: Double = 0.0

        init {
            animatableMap.putAll(mapOf(
                "radius" to ::radius,
            ))
        }

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
                drawer.circle(x * program.width, y * program.height, radius)
            }
        }
    }

}