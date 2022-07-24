package rain.language

import rain.interfaces.*

class Node(
    override val key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf()
): GraphableNode, LanguageItem {

    override val properties = properties.toMutableMap()

    // TODO: replace with label instance
    override val labels get() = listOf(this::class.simpleName ?: "")

    override val primaryLabel get() = this.labels[0]

}