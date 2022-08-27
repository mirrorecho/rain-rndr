package rain.patterns

import rain.interfaces.*
import rain.language.*

// a node that represents an iterable over a group nodes ... each of which is connected
// to this node, in a "pattern"
// TODO maybe: is Pattern really an interface
open class Tree(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Pattern, Node(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Tree> = Label(
            factory = { k, p, c -> Tree(k, p, c) },
            labels = listOf("Tree", "Pattern"),
        )
    }

    override val isAlter = false

    override val isLeaf = false

    override val label: LabelInterface get() = Tree.label

    // TODO: make these by lazy

    override val branches: TreeBranchesSelect get() = TreeBranchesSelect(context, this)

    override val nodes: TreeNodesSelect get() = TreeNodesSelect(context, this)

    override val leaves: TreeLeavesSelect get() = TreeLeavesSelect(context, this)

    val isEmpty: Boolean get() = r(SelectDirection.RIGHT, "CUES_FIRST").first == null

    fun extend(vararg patterns: Pattern) {

        // creates all Cue nodes for the extension (inc. Contains and Cues relationships)
        val cueNodes = patterns.map {
            val cue = Cue().createMe() as Cue
            Contains(source_key = this.key, target_key = cue.key).createMe()
            Cues(source_key = cue.key, target_key = it.key).createMe()
            cue
        }

        if (isEmpty)
            // if empty, then create the CuesFirst
                // note... empty check works even after creating the Contains relationships above
                // because the isEmpty logic checks for CUES_FIRST
            CuesFirst(source_key = this.key, target_key = cueNodes[0].key).createMe()
        else {
            // otherwise create a CuesNext relationship from the existing CuesLast target node to the start of extension cue nodes
            // and remove the the CuesLast
            val cuesLast = r(SelectDirection.RIGHT, "CUES_LAST").first!! as Relationship
            CuesNext(source_key = cuesLast.target_key, target_key = cueNodes[0].key).createMe()
            cuesLast.delete()
        }

        // creates CuesNext relationships between all the Cue nodes
        cueNodes.asIterable().zipWithNext { c, cNext ->
            CuesNext(source_key = c.key, target_key = cNext.key).createMe()
        }

        // adds CuesLast relationship at the end
        CuesLast(source_key = this.key, target_key = cueNodes.last().key).createMe()

    }
}

// ===========================================================================================================

open class CellTree(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): CellPattern, Tree(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<CellTree> = Label(
            factory = { k, p, c -> CellTree(k, p, c) },
            labels = listOf("CellTree", "Tree", "CellPattern", "Pattern"),
        )
    }

    override var simultaneous: Boolean by this.properties.apply { putIfAbsent("simultaneous", false) }

//    // TODO: not elegant!
//    override fun setInitProperties(existingProperties: MutableMap<String, Any?>) {
//        super.setInitProperties(existingProperties)
//        existingProperties.putIfAbsent("simultaneous", false)
//    }

    override val label: LabelInterface get() = CellTree.label

    override val veins: Sequence<MutableMap<String, Any?>> get() = sequence {
        leaves.asTypedSequence<Cell>().forEach { c->
            yieldAll( c.veins.map {
                it.u
            } )
        }
    }

}
