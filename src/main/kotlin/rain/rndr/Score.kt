package rain.rndr

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.launch
import rain.language.Palette
import rain.patterns.CellPattern
import rain.utils.autoKey
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class Score(
    val machinePalette: Palette<MachineFunc>,
    val program: Program
) {
    private val actions: MutableMap<String, Act> = mutableMapOf()
    private val timeCodes: MutableMap<Double, MutableList<MachineAction>> = mutableMapOf()

    fun updateOrCreateAct(machineFunc: MachineFunc, actName: String): Act{ //TODO maybe: machine better here than machineName?
        val act = actions.getOrPut(actName) {
            Act(
                actName,
                machineFunc,
                machineFunc.properties.toMutableMap(),
                this
                // TODO: implement map of machine relationships to acts
            )
        }
        return act
    }

    private fun createAction(time:Double, properties: Map<String, Any?>) {
        val timeCodeList = timeCodes.getOrPut(time) {mutableListOf()}

        // TODO:
        // determine whether to create or update (or delete as an "update")

        // create a new act
        // TODO: make this less gross (all the type casting sucks)
        // TODO maybe: move implementation to MachineFunc? (esp. if it helps with implementing map of
        //  machine relationships to acts below)
        val act = Act(
            properties.getOrDefault("act", autoKey()) as String,
            machinePalette[properties["machine"] as String] as MachineFunc, // TODO: throw warning or error if machine not found
            properties.toMutableMap(),
            this
            // TODO: implement map of machine relationships to acts
        )
        timeCodeList.add(act)

        // TODO:
        // update an act
    }

    // TODO: test and document
    fun createActions(pattern: CellPattern, runningTime:Double=0.0): Double {

        // TODO: refactor and simplify this logic...? (also look at old python code)
        var patternDur = 0.0
        println(pattern)
        if (pattern.isLeaf) {
            // TODO maybe: handle fancy logic like hooks here?
            pattern.veins.forEach {
                val veinDur = it["dur"] as Double
                if (pattern.simultaneous) {
                    createAction(runningTime, it)
                    if (veinDur > patternDur) patternDur = veinDur
                } else {
                    createAction(runningTime+patternDur, it)
                    patternDur += veinDur
                }
            }
        } else {
            pattern.branches.asTypedSequence<CellPattern>().forEach { branch ->
                println("yo branch " + branch.toString())
                if (pattern.simultaneous) {
                    val branchEndTime = createActions(branch, runningTime)
                    val branchDur = branchEndTime-runningTime
                    if (branchDur > patternDur) patternDur = branchDur
                } else {
                    val branchEndTime = createActions(branch, runningTime+patternDur)
                    patternDur = branchEndTime-runningTime
                }
            }
        }
        return runningTime + patternDur
    }

    fun reset() {
        actions.clear()
        timeCodes.clear()
    }

    fun play() = application {
        reset()
        var prevTriggerTime = 0.0
//        println(timeCodes.toSortedMap())

        program {

            launch {
                timeCodes.keys.sorted().forEach { actionTime ->
                    val actionList = timeCodes[actionTime]!!
                    val delayTime = actionTime - prevTriggerTime
                    if (delayTime > 0) delay((delayTime).toDuration(DurationUnit.SECONDS))
                    launch {
                        actionList.forEach { action ->
                            if (action is Act) {
                                action.start()
                                // println("starting: " + action.toString())
                            }
                            // TODO: implement Update actions here (inc. stopping at appropriate dur)
//                            else {
//                                if (p["gate"] != true) launch {
//                                    // TODO: consider accommodating ops with indeterminate durs...
//                                    delay((p["dur"] as Double).toDuration(DurationUnit.SECONDS))
//                                    machine.stopOp(op)
//                                }
//                            }
                        }
                    }
                    prevTriggerTime = actionTime
                }
            }

            extend {

                machinePalette.context.cycleOps()

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