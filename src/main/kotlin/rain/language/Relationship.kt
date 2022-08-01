package rain.language

import rain.interfaces.*

open class Relationship(
    key:String = rain.utils.autoKey(),
    override val source: Node,
    override val target: Node,
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): GraphableRelationship, Item(key, properties, context) {

    val relationshipType: String get() = this::class.simpleName ?: ""

    // TODO: replace with label instance
    override val labels get() = listOf(this.relationshipType)

    override val primaryLabel get() = this.relationshipType


}