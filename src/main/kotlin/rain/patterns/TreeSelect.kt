package rain.language

import rain.interfaces.*
import rain.patterns.*

abstract class TreeSelect(
    context:ContextInterface,
    protected val selfNode: Tree,
    ):Select(context = context) {

    // TODO maybe... by lazy appropriate here? (assume yes)
    override val keys by lazy { asSequence().map{it.key}.toList() }

    // child classes must implement asSequence to avoid problems!
    override fun asSequence(): Sequence<Pattern> = getBranchCues().map { throw(NotImplementedError()) }

    fun getBranchCues(): Sequence<Cue> = sequence {
        this@TreeSelect.selfNode.r(SelectDirection.RIGHT, "CUES_FIRST").n().first?.let {
            var branchCue = it as Cue?
            while (branchCue != null) {
//                println(branchCue)
                yield(branchCue)
                branchCue = branchCue.r(SelectDirection.RIGHT, "CUES_NEXT").n().first as Cue?
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
        yield(selfNode)
        this@TreeNodesSelect.getBranchCues().forEach {
            yieldAll(it.cuesPattern.nodes.asTypedSequence())
        }
    }
}

