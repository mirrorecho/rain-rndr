package rain.rndr

import org.openrndr.Program

//abstract class MachineInstance(
//    val machine: RndrMachine,
//    val program: Program, // TODO: needed?
//    val properties: Map<String, Any>,
//    ) {
//
//    var running = false
//
//    val dur:Double get() = this.properties["dur"] as Double
//
//    fun start(): MachineInstance {
//        running = true
//        return this
//    }
//
//    fun stop(): MachineInstance {
//        running = false
//        machine.cleanup(this)
//        return this
//    }
//
//}