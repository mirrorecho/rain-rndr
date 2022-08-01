package rain.language

import rain.interfaces.*

open class Node(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): GraphableNode, Item(key, properties, context) {

    override val properties = properties.toMutableMap()

    // TODO: replace with label instance
    override val labels get() = listOf(this::class.simpleName ?: "")

    override val primaryLabel get() = this.labels[0]

//    fun r

}