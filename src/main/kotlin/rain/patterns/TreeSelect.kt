package rain.language

import rain.interfaces.*
import rain.patterns.*

abstract class TreeSelect(
    context:ContextInterface,
    protected val selfNode: Tree,
    ):Select(context = context) {

    fun getBranchCues(): Sequence<Cue> = sequence {
        this@TreeSelect.selfNode.r(SelectDirection.RIGHT, "CUE_FIRST").first?.let {
            var childCue = it as Cue?
            while (childCue != null) {
                yield(childCue)
                childCue = it.r(SelectDirection.RIGHT, "CUE_NEXT").n().first as Cue?
            }
        }
    }
}
// ===========================================================================================================

open class TreeBranchesSelect(
    context:ContextInterface,
    selfNode: Tree,
):TreeSelect(context = context, selfNode=selfNode) {

    // TODO: handle branch hooks (eiter here or in tree)
    override fun asSequence(): Sequence<Pattern> = getBranchCues().map { it.cuesPattern }

}
// ===========================================================================================================

open class TreeLeavesSelect(
    context:ContextInterface,
    selfNode: Tree,
):TreeSelect(context = context, selfNode=selfNode) {

    // TODO: handle branch hooks (eiter here or in tree)
    override fun asSequence(): Sequence<Leaf> = sequence {
        this@TreeLeavesSelect.getBranchCues().forEach { yieldAll(it.cuesPattern.leaves.asTypedSequence()) }
    }
}
// ===========================================================================================================

open class TreeNodesSelect(
    context:ContextInterface,
    selfNode: Tree,
):TreeSelect(context = context, selfNode=selfNode) {

    // TODO: handle branch hooks (eiter here or in tree)
    override fun asSequence(): Sequence<Pattern> = sequence {
        this@TreeNodesSelect.getBranchCues().forEach {
            yield(it.cuesPattern)
            yieldAll(it.cuesPattern.nodes.asTypedSequence())
        }
    }
}

