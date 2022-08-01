package rain.language

import rain.interfaces.*

abstract class Item(
    override val key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    override val context: ContextInterface = LocalContext
): LanguageItem {

//    abstract companion object {
//        val label: LabelInterface
////        fun make(key: String = rain.utils.autoKey()): Item {
////            return Item(key)
////        }
//    }

    override val properties = properties.toMutableMap()

    override fun save() {}

    override fun read() {}

    override fun delete() {}

    override fun mergeMe() {}

    override fun deleteMe() {}




}