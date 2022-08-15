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
): CellPattern, Leaf(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Cell> = Label(
            factory = { k, p, c -> Cell(k, p, c) },
            labels = listOf("Cell", "Leaf", "CellPattern", "Pattern"),
        )
    }
    override val label: LabelInterface get() = Cell.label

    override val isAlter = false

    var traverseNames = listOf("dur", "machine")

//    val dur: Sequence<Int> = sequenceOf(1).cycle()
//    val dur: Sequence<Int> = sequenceOf(1).cycle()

    override fun <T>propertyByVein(key: String): Sequence<T> = this.properties[key] as Sequence<T>

    // TODO: implement something like this for setting PARTIAL sequences of values
//    override fun setPropertyByVein(key: String, values:Sequence<Any>) {
//        veins.zip(values).forEach { it.first[key] = it.second }
//    }

    override val veins: Sequence<MutableMap<String, Any>> get() = sequence {
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
