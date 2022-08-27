package rain.patterns

import rain.interfaces.*
import rain.language.*

// a node that represents an iterable over a group nodes ... each of which is connected
// to this node, in a "pattern"
// TODO maybe: is Pattern really an interface
open class Leaf(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Pattern, Node(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Leaf> = Label(
            factory = { k, p, c -> Leaf(k, p, c) },
            labels = listOf("Leaf", "Pattern"),
        )
    }
    override val label: LabelInterface get() = Leaf.label


    // TODO: implement the below
//    # TODO: assume this doesn't need to be serialized?
//    leaf_hooks: Iterable[Callable[["rain.Pattern", "rain.Pattern"], "rain.Pattern"]] = ()
//    vein_hooks: Iterable[Callable[["rain.Pattern", Any, int], Any]] = ()
//    _parentage = ()
//    # TODO: MAYBE consider this
//    # node_hooks: Iterable[Callable[["rain.Pattern", "rain.Pattern"], "rain.Pattern"]] = ()
//

    override val isAlter = false

    override val isLeaf = true

    override val branches = EmptySelect(context)

    // TODO: implement hook logic here
    override val leaves get() = SelfSelect(context, this)

    override val nodes get() = SelfSelect(context, this)

}
