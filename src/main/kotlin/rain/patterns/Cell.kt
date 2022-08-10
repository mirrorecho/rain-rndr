package rain.patterns

import rain.interfaces.*
import rain.language.*
import rain.utils.cycle

// a node that represents an iterable over a group nodes ... each of which is connected
// to this node, in a "pattern"
// TODO maybe: is Pattern really an interface
open class Cell(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context: ContextInterface = LocalContext,
): Leaf(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Cell> = Label(
            factory = { k, p, c -> Cell(k, p, c) },
            labels = listOf("Cell", "Leaf", "Pattern"),
        )
    }
    override val label: LabelInterface get() = Cell.label

    val simultaneous: Boolean = false

    var traverseNames = listOf("dur", "machine")

    // TODO: maybe move these to a "MachineCell" child class?
    // and TODO: would be ideal to set defaults to these
    // and TODO: how to handle lambdas?
    var dur:Sequence<Int>
        get() = getAs("dur")
        set(s) { this["dur"] = s }

    var machine:Sequence<String>
        get() = getAs("machine")
        set(s) { this["machine"] = s }

//    val dur: Sequence<Int> = sequenceOf(1).cycle()
//    val dur: Sequence<Int> = sequenceOf(1).cycle()

    override val veins: Sequence<Map<String, Any>> get() = sequence {
        var returnMap = mutableMapOf<String, Any>()
        var returning = true
        var namesIterators: List<Pair<String, Iterator<Any?>>> = traverseNames.map {
            Pair(it, this@Cell.getAs<Sequence<*>>(it).iterator())
        }
        while (returning) {
            namesIterators.forEach {
                if (it.second.hasNext()) returnMap[it.first] = it.second.next() as Any
                else returning = false
            }
            if (returning) yield(returnMap)
        }
    }


}
