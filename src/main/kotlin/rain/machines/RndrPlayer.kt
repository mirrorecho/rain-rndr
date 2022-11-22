package rain.machines

import kotlinx.coroutines.*
import rain.interfaces.*
import rain.language.*
import rain.patterns.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

import org.openrndr.application
import org.openrndr.Program
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorRGBa
import org.openrndr.launch
import rain.rndr.RndrMachine

// TODO: implement tempos
// TODO: refactor to inherit from Player
class RndrPlayer(
    val cellPattern: CellPattern,
    val machinePalette: Palette<RndrMachine>,
) {

    // TODO maybe: instead of just a list, this could be something like a timed trigger bundle? (inc. max dur predefined?)
    private val triggers: MutableMap<Double, MutableList<Map<String, Any?>>> = mutableMapOf()

    private fun setTrigger(time:Double, properties: Map<String, Any?>) {
        val timeTriggerList = triggers.getOrPut(time) {mutableListOf()}
        timeTriggerList.add(properties)
    }

    // TODO: test and document
    private fun setTriggers(pattern:CellPattern, startTime:Double=0.0): Double {
        var runningTime = startTime
        // TODO: refactor and simplify this logic...? (also look at old python code)
        var patternDur = 0.0
        if (pattern.isLeaf) {
            // TODO maybe: handle fancy logic like hooks here?
            pattern.veins.forEach {
                val veinDur = it["dur"] as Double
                if (pattern.simultaneous) {
                    setTrigger(runningTime, it)
                    if (veinDur > patternDur) patternDur = veinDur
                } else {
                    setTrigger(runningTime+patternDur, it)
                    patternDur += veinDur
                }
            }
        } else {
            pattern.branches.asTypedSequence<CellPattern>().forEach { branch ->
                if (pattern.simultaneous) {
                    val branchEndTime = setTriggers(branch, runningTime)
                    val branchDur = branchEndTime-runningTime
                    if (branchDur > patternDur) patternDur = branchDur
                } else {
                    val branchEndTime = setTriggers(branch, runningTime+patternDur)
                    patternDur = branchEndTime-runningTime
                }
            }
        }
        return runningTime + patternDur
    }

    fun reset() {
        triggers.clear()
    }

    fun play() = application {
        reset()
        // TODO maybe - auto-populate machinePalette?
        // TODO maybe - don't set all triggers upfront? ... maybe just launch co-routines ..as needed?
        setTriggers(this@RndrPlayer.cellPattern)
        var prevTriggerTime = 0.0
        println(triggers.toSortedMap())


        program {

            launch {
                triggers.keys.sorted().forEach { triggerTime ->
                    val triggerList = triggers[triggerTime]!!
                    val delayTime = triggerTime - prevTriggerTime
                    if (delayTime > 0) delay((delayTime).toDuration(DurationUnit.SECONDS))
                    launch {
                        triggerList.forEach { p ->
                            val machine = this@RndrPlayer.machinePalette[p["machine"] as String]
                            val op = machine.triggerOn(triggerTime, this@program, p)
                            launch {
                                // TODO: consider accommodating ops with indeterminate durs...
                                delay((op.dur).toDuration(DurationUnit.SECONDS))
                                machine.triggerOff(op)
                            }
                        }
                    }
                    prevTriggerTime = triggerTime
                }
            }

            extend {

                machinePalette.forEach { it.render() }

                // DO NOTHING?

//                animation.updateAnimation()
//                if (!animation.hasAnimations()) {
//                    animation.apply {
//                        ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
//                        ::x.complete()
//                        ::x.animate(0.0, 1000, Easing.CubicInOut)
//                        ::x.complete()
//                    }
//                }
//                drawer.fill = myColor
//                drawer.stroke = null
//                drawer.circle(animation.x, height / 2.0, 100.0)
            }

        }
    }

}