package rain.patterns

import rain.interfaces.*
import rain.language.Select
import rain.patterns.*

abstract class TreeSelect(
    context:ContextInterface,
    protected val selfNode: Tree,
    ): Select(context = context) {

    // TODO maybe... by lazy appropriate here? (assume yes)
    override val keys by lazy { asSequence().map{it.key}.toList() }

    // child classes must implement asSequence to avoid problems!
    override fun asSequence(): Sequence<Pattern> = getBranchCues().map { throw(NotImplementedError()) }

    fun getCachedParentage() = this.selfNode.cachedParentage + listOf(this.selfNode)

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
): TreeSelect(context = context, selfNode=selfNode) {

    // TODO: handle branch hooks (either here or in tree)
    // TODO: test cached parentage
    override fun asSequence(): Sequence<Pattern> = getBranchCues().map {
        it.cuesPattern.apply { cachedParentage = this@TreeBranchesSelect.getCachedParentage() }
    }

}
// ===========================================================================================================

open class TreeLeavesSelect(
    context:ContextInterface,
    selfNode: Tree,
): TreeSelect(context = context, selfNode=selfNode) {

    // TODO: handle branch hooks (eiter here or in tree)
    override fun asSequence(): Sequence<Leaf> = sequence {
        // TODO: test cached parentage
        this@TreeLeavesSelect.getBranchCues().forEach {
            yieldAll(it.cuesPattern.apply {
                cachedParentage = this@TreeLeavesSelect.getCachedParentage()
            }.leaves.asTypedSequence())
        }
    }
}
// ===========================================================================================================

open class TreeNodesSelect(
    context:ContextInterface,
    selfNode: Tree,
): TreeSelect(context = context, selfNode=selfNode) {

    // TODO: handle branch hooks (eiter here or in tree)
    override fun asSequence(): Sequence<Pattern> = sequence {
        // TODO: test cached parentage
        yield(selfNode)
        this@TreeNodesSelect.getBranchCues().forEach {
            yieldAll(it.cuesPattern.apply {
                cachedParentage = this@TreeNodesSelect.getCachedParentage()
            }.nodes.asTypedSequence())
        }
    }
}

