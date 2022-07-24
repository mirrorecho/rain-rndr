package rain.language

import rain.interfaces.GraphableItem

abstract class Item(
    override val key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf()
): GraphableItem {

//    companion object {
//        fun make(key: String = rain.utils.autoKey()): Item {
//            return Item(key)
//        }
//    }

    override val properties = properties.toMutableMap()

    // TODO include parent classes in label list
    override val labels: List<String> get() = listOf(this.primaryLabel)

    override val primaryLabel: String get() = this::class.simpleName ?: ""

    abstract fun save()

    abstract fun read()

    abstract fun delete()

    abstract fun mergeMe()

    abstract fun deleteMe()

}