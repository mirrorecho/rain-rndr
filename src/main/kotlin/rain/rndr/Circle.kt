package rain.rndr


import rain.interfaces.ContextInterface
import rain.language.LocalContext



open class Circle(
    name:String = rain.utils.autoKey(),
    override val rndrMachine: RndrMachine<Act>,
    properties: MutableMap<String, Any?> = mutableMapOf(),
    score: Score,
    context: ContextInterface = LocalContext,
): Act(name, rndrMachine, properties, score) {

    // TODO MAYBE: a base drawing class with standard attributes like color, position, etc.

    // TODO... how can this factory work given the val parameters above??!!
//    override val label = LocalContext.getLabel("Circle", "RndrMachine", "MachineFunc", "Machine", "Leaf") { k, p, c -> Circle(k, p, c) }

    val radius:MachineProperty<Double> = MachineProperty("radius")
    val position: MachineProperty<Position> = MachineProperty("radius")
    val strokeColor:MachineProperty<Color?> = MachineProperty("strokeColor")
    val strokeWeight:MachineProperty<Double> = MachineProperty("strokeWeight")
    val fillColor:MachineProperty<Color?> = MachineProperty("fillColor")

    // TODO: accommodate local storage...
    //  ... point to objects that could EITHER represent
    //  - machine nodes
    //  - OR simple values (from this node's properties)
    //  - OR collections of values (from this node's properties)
    // TODO: or maybe by lazy is not ideal here? think about it...


    override var renderAct: (Act)->Unit = { act ->
        act.program.apply {
            // TODO: pass separate keys for each op
            drawer.stroke = strokeColor.getValue(act)?.colorRGBa(act) // TODO: is this second act passed here correct?
            drawer.strokeWeight = strokeWeight.getValue(act) // TODO ditto...
            drawer.fill = fillColor.getValue(act)?.colorRGBa(act) // TODO ditto...
            drawer.circle(
                position.getValue(act).vector(act), // TODO ditto...
                radius.getValue(act)
            )
        }
    }

}