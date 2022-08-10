package rain.language

import rain.interfaces.*

open class Node(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): GraphableNode, Item(key, properties, context) {

    companion object: ItemCompanion() {
        override val label: Label<Node> = Label(
            factory = { k, p, c -> Node(k, p, c) },
            labels = listOf("Node"),
        )
    }
    override val label: LabelInterface get() = Node.label

    fun r(direction:SelectDirection, label:String?=null, keys:List<String>?=null, properties:Map<String,Any>?=null):TargetedRelationshipSelect =
        TargetedRelationshipSelect(context=this.context, label=label, keys=keys, properties=properties,
            selectFrom=selectSelf, direction=direction)
}