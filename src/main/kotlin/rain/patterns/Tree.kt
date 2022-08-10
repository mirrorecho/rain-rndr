package rain.patterns

import rain.interfaces.*
import rain.language.*

// a node that represents an iterable over a group nodes ... each of which is connected
// to this node, in a "pattern"
// TODO maybe: is Pattern really an interface
open class Tree(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context: ContextInterface = LocalContext,
): Pattern(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Tree> = Label(
            factory = { k, p, c -> Tree(k, p, c) },
            labels = listOf("Tree", "Pattern"),
        )
    }

    override val isLeaf = false

    override val label: LabelInterface get() = Tree.label

    // TODO: make these by lazy

    override val branches: TreeBranchesSelect get() = TreeBranchesSelect(context, this)

    override val nodes: TreeNodesSelect get() = TreeNodesSelect(context, this)

    override val leaves: TreeLeavesSelect get() = TreeLeavesSelect(context, this)

    override val veins: Sequence<Map<String, Any>> get() = sequence {
        leaves.asTypedSequence<Leaf>().forEach { yieldAll(it.veins) }
    }


}
