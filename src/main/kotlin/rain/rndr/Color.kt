package rain.rndr

import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import rain.interfaces.ContextInterface
import rain.language.LocalContext

open class Color(
    name:String = rain.utils.autoKey(),
    override val rndrMachine: RndrMachine<Color>,
    properties: MutableMap<String, Any?> = mutableMapOf(),
    score: Score,
): Act(name, rndrMachine, properties, score) {

    // TODO: accommodate local storage
    val h: ValueFunc by lazy { targetsAs("COLOR_H") }
    val s: ValueFunc by lazy { targetsAs("COLOR_S") }
    val v: ValueFunc by lazy { targetsAs("COLOR_V") }
    val a: ValueFunc by lazy { targetsAs("COLOR_A") }

    override val label = LocalContext.getLabel("Color", "MachineFunc", "Machine", "Leaf") { k, p, c -> Color(k, p, c) }

    fun colorRGBa(act: Act): ColorRGBa = ColorHSVa(
        // TODO: accommodate local storage
        act.relatedVal("COLOR_H"),
        act.relatedVal("COLOR_S"),
        act.relatedVal("COLOR_V"),
        act.relatedVal("COLOR_A"),

//        h.actVal(act.getRelatedAct("COLOR_H")),
//        s.actVal(act.getRelatedAct("COLOR_S")),
//        v.actVal(act.getRelatedAct("COLOR_V")),
//        a.actVal(act.getRelatedAct("COLOR_A")),
    ).toRGBa()

}