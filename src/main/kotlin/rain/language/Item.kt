package rain.language

import rain.interfaces.*

abstract class ItemCompanion {
    abstract val label: LabelInterface
}

// ===========================================================================================================

abstract class Item(
    override val key:String,
    properties: Map<String, Any>,
    override val context: ContextInterface
): LanguageItem {

    override val properties = properties.toMutableMap()

    override val selectSelf: Select get() = SelfSelect(this.context, this)

}

// ===========================================================================================================

open class Node(
    override val key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    override val context: ContextInterface = LocalContext,
): LanguageNode, Item(key, properties, context) {

    companion object: ItemCompanion() {
        override val label: Label<Node> = Label(
            factory = { k, p, c -> Node(k, p, c) },
            labels = listOf("Node"),
        )
    }
    override val label: LabelInterface get() = Node.label

    override fun r(direction: SelectDirection, label:String?, keys:List<String>?, properties:Map<String,Any>?): TargetedRelationshipSelect =
        selectSelf.r(direction=direction, label=label, keys=keys, properties=properties)

    override fun targets(label:String?, keys:List<String>?, properties:Map<String,Any>?): TargetedRelationshipSelect =
        r(direction= SelectDirection.RIGHT, label=label, keys=keys, properties=properties)

}

// ===========================================================================================================

open class Relationship(
    key:String = rain.utils.autoKey(),
    var source_key: String?, // TODO: this is wonky... really, key string should be required
    var target_key: String?, // ditto
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): LanguageRelationship, Item(key, properties, context) {

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