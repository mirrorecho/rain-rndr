package rain.language

import rain.interfaces.*

class Relationship(
    override val key:String = rain.utils.autoKey(),
    override val source: Node,
    override val target: Node,
    properties: Map<String, Any> = mapOf()
): GraphableRelationship, LanguageItem {

    override val properties = properties.toMutableMap()

    val relationshipType: String get() = this::class.simpleName ?: ""

    // TODO: replace with label instance
    override val labels get() = listOf(this.relationshipType)

    override val primaryLabel get() = this.relationshipType


}