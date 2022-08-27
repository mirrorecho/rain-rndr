package rain.patterns

import rain.interfaces.*
import rain.language.*

// a node that represents an iterable over a group nodes ... each of which is connected
// to this node, in a "pattern"
// TODO maybe: is Pattern really an interface
open class Cell(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
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

    // the following probably not even needed since dur.sum() is so easy!
//    val sumDur: Double get() = dur.sum()

    override var dur: Sequence<Double> by this.properties
    override var machine: Sequence<String> by this.properties

//    val dur: Sequence<Int> = sequenceOf(1).cycle()
//    val dur: Sequence<Int> = sequenceOf(1).cycle()

    override fun <T>propertyByVein(key: String): Sequence<T> = this.properties[key] as Sequence<T>

    // TODO: implement something like this for setting PARTIAL sequences of values
//    override fun setPropertyByVein(key: String, values:Sequence<Any>) {
//        veins.zip(values).forEach { it.first[key] = it.second }
//    }

    override var simultaneous: Boolean by this.properties.apply { putIfAbsent("simultaneous", false) }

//    // TODO: not elegant!
//    override fun setInitProperties(existingProperties: MutableMap<String, Any?>) {
//        super.setInitProperties(existingProperties)
//        existingProperties.putIfAbsent("simultaneous", false)
//    }


    override val veins: Sequence<MutableMap<String, Any?>> get() = sequence {
        var returning = true
        val namesIterators: List<Pair<String, Iterator<Any?>>> = traverseNames.map {
            Pair(it, this@Cell.getAs<Sequence<*>>(it).iterator())
        }
        while (returning) {
            val returnMap = mutableMapOf<String, Any?>()
            namesIterators.forEach {
                if (it.second.hasNext()) returnMap[it.first] = it.second.next() as Any
                else returning = false
            }
            if (returning) yield(returnMap)
        }
    }


}
