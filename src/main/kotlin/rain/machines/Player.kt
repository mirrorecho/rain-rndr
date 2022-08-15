package rain.machines

import rain.interfaces.*
import rain.language.*
import rain.patterns.*
import java.util.*

class Player(
    val cellPattern: CellPattern,
    val machinePalette: Palette<Machine>,
) {

    private val triggers: MutableMap<Double, MutableList<Map<String, Any>>> = mutableMapOf()

    private fun setTrigger(time:Double, properties: Map<String, Any>) {
        val timeTriggerList = triggers.getOrPut(time) {mutableListOf()}
        timeTriggerList.add(properties)
    }

    // TODO: test and document
    private fun setTriggers(pattern:CellPattern, startTime:Double=0.0): Double {
        var runningTime = startTime
        if (pattern.isLeaf) {
            // TODO maybe: handle fancy logic like hooks here?
            pattern.veins.forEach {
                setTrigger(runningTime, it)
                runningTime += it["dur"] as Double}
        } else {
            pattern.branches.asTypedSequence<CellPattern>().forEach {
                val branchEndTime = setTriggers(it, runningTime)
                if (!pattern.simultaneous || branchEndTime > runningTime) runningTime = branchEndTime
            }
        }
        return runningTime
    }

    fun reset() {
        triggers.clear()
    }

    fun play() {
        reset()
    }
}