package rain.machines

import kotlinx.coroutines.*
import rain.interfaces.*
import rain.language.*
import rain.patterns.*
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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

    fun play() = runBlocking {
        reset()
        // TODO maybe - auto-populate machinePalette?
        setTriggers(this@Player.cellPattern)
        var runningTime = 0.0
        println(triggers.toString())
        triggers.keys.sorted().forEach {
            delay((it-runningTime).toDuration(DurationUnit.SECONDS) )
            launch { triggerAt(it, triggers[it]!!) }
            runningTime += it
        }
    }

    fun triggerAt(runningTime: Double, triggerList: List<Map<String, Any>>) {
        triggerList.forEach { p ->
            this@Player.machinePalette[p["machine"] as String].trigger(runningTime, p)
        }
    }
}