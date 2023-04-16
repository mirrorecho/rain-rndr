package plum

import org.openrndr.Program
import org.openrndr.draw.LineCap
import org.openrndr.extra.noise.random
import rain.interfaces.ContextInterface
import rain.interfaces.LabelInterface
import rain.language.ItemCompanion
import rain.language.Label
import rain.language.LocalContext
import rain.rndr_bak.RndrMachine
import rain.rndr_bak.Line
import rain.rndr_bak.RndrOp
import rain.rndr_bak.Line.LineOp

open class LineSwirl(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Line(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<LineSwirl> = Label(
            factory = { k, p, c -> LineSwirl(k, p, c) },
            labels = listOf("LineSwirl", "RndrShape", "RndrMachine", "Machine"),
        )
    }

    override val label: LabelInterface get() = LineSwirl.label

    override fun opFactory(machine: RndrMachine, program: Program, properties: MutableMap<String, Any?>): RndrOp {
        return LineSwirlOp(machine as LineSwirl, program, properties.toMutableMap())
    }

    // NOTE: unlike in line... x, y, x2, y2 are used as bounds for randomizing the lines endpoints

    class LineSwirlOp(
        override val machine: LineSwirl,
        program: Program,
        properties: MutableMap<String, Any?>,
    ): LineOp(machine, program, properties) {


        override fun reTrigger(properties: Map<String, Any?>) {
            super.reTrigger(
                properties.toMutableMap().apply {
                    putAll(
                        mapOf(
                            "x" to random(this["x"] as Double, this["x2"] as Double),
                            "x2" to random(this["x"] as Double, this["x2"] as Double),
                            "y" to random(this["y"] as Double, this["y2"] as Double),
                            "y2" to random(this["y"] as Double, this["y2"] as Double),
                        )
                    )
                }
            )
        }

        override fun render() {
            this.program.apply {
                drawer.lineCap = LineCap.ROUND
            }
            super.render()

        }
    }
}