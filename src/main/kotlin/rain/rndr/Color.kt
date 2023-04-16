package rain.rndr

import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import rain.interfaces.ContextInterface
import rain.language.LocalContext

open class Color(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): MachineFunc(key, properties, context) {
    val h: ValueFunc by lazy { targetsAs("COLOR_H") }
    val s: ValueFunc by lazy { targetsAs("COLOR_S") }
    val v: ValueFunc by lazy { targetsAs("COLOR_V") }
    val a: ValueFunc by lazy { targetsAs("COLOR_A") }

    override val label = LocalContext.getLabel("Color", "MachineFunc", "Machine", "Leaf") { k, p, c -> Color(k, p, c) }

    fun colorRGBa(opKey:String): ColorRGBa = ColorHSVa(
        // TODO: these need to be separate keys!!!
        h.opVal(opKey), s.opVal(opKey), v.opVal(opKey), a.opVal(opKey)
    ).toRGBa()

}