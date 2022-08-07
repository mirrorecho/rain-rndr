package rain.language

import rain.interfaces.*

abstract class ItemCompanion {
    abstract val label: LabelInterface
}


abstract class Item(
    override val key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    override val context: ContextInterface = LocalContext
): LanguageItem {

    override val properties = properties.toMutableMap()

    override fun save(): Item {
        context.graph.save(this)
        return this
    }

    override fun read(): Item {
        context.graph.read(this)
        return this
    }

    override fun delete() {
        context.graph.delete(this.key)
    }

    override fun mergeMe(): Item {
        context.graph.merge(this)
        return this
    }

    override fun createMe(): Item {
        context.graph.create(this)
        return this
    }




}