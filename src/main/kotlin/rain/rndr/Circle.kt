package rain.rndr


import rain.interfaces.ContextInterface
import rain.language.LocalContext



open class Circle(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): RndrMachine(key, properties, context) {

    // TODO MAYBE: a base drawing class with standard attributes like color, position, etc.

    override val label = LocalContext.getLabel("Circle", "RndrMachine", "MachineFunc", "Machine", "Leaf") { k, p, c -> Circle(k, p, c) }

    // TODO: accommodate local storage...
    //  ... point to objects that could EITHER represent
    //  - machine nodes
    //  - OR simple values (from this node's properties)
    //  - OR collections of values (from this node's properties)
    // TODO: or maybe by lazy is not ideal here? think about it...

//    val radius: ValueFunc by lazy { targetsAs("RADIUS") }
    val radius = MachineProperty(this, "RADIUS")

    val position: Position by lazy { targetsAs("POSITION") }
    val strokeColor: Color? by lazy { targetsAs("STROKE_COLOR") }
    val strokeWeight: ValueFunc by lazy { targetsAs("STROKE_WEIGHT") }
    val fillColor: Color? by lazy { targetsAs("FILL_COLOR") }

    override var renderAct: (Act)->Unit = { act ->
        act.program.apply {
            // TODO: pass separate keys for each op
            drawer.stroke = strokeColor?.colorRGBa(act.relatedAct("STROKE_COLOR"))
            drawer.strokeWeight = strokeWeight.actVal(act.relatedAct("STROKE_WEIGHT"))
            drawer.fill = fillColor?.colorRGBa(act.relatedAct("FILL_COLOR"))
            drawer.circle(
                position.vector(act.relatedAct("POSITION")),
                act.relatedVal("RADIUS")
            )
        }
    }

}