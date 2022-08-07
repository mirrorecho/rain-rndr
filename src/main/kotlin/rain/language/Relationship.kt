package rain.language

import rain.interfaces.*

open class Relationship(
    key:String = rain.utils.autoKey(),
    var source_key: String?, // TODO: this is wonky... really, key string should be required
    var target_key: String?, // ditto
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): GraphableRelationship, Item(key, properties, context) {

    // TODO: elvis operator defaulting to empty string is wonky here... should just alwyas have the key
    // and TODO maybe: should these be read upfront when initialized?
    override val source: Node by lazy { Node(source_key ?: "") }
    override val target: Node by lazy { Node(target_key ?: "") }

    companion object: ItemCompanion() {
        override val label: Label<Relationship> = Label(
            factory = { k, p, c -> Relationship(k, null, null, p, c) },
            labels = listOf("RELATES_TO"),
        )
    }
    override val label: LabelInterface get()  = Relationship.label




}