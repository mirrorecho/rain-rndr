package rain.rndr

import rain.utils.*
import rain.interfaces.*
import rain.language.*
import  rain.machines.*
import rain.patterns.*

// OLD TODOS:
// TODO: combine Machine and MachineFunc?
// TODO: plan for connecting MachineFuncs to Machine via relationships
// TODO: maybe this class should be abstract?
// TODO: does this class have any purpose at all anymore now that renderOp also implemented on machineFunc?


// TODO: reconfigure so Act type param not needed at class level, only at fun level
open class RndrMachine(
    key:String = autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) { // TODO: is Leaf the best parent class? (Relationships might not be simple tree patterns.)

    override val label = LocalContext.getLabel("RndrMachine", "Machine", "Leaf") { k, p, c -> RndrMachine(k, p, c) }

    val actFactory: (tr:Trigger)->Unit get() = getFancyProperty< (tr:Trigger)->Unit >("ACT_FACTORY").value

    var single: Boolean by this.properties

    fun setFactory(factory: (tr:Trigger)->Unit) {
        this.setProperty("ACT_FACTORY", factory, true )
    }

    // TODO: acts of a particular type should no longer be associated with particular RndrMachines
//    fun <RT:Act>getRelatedAct(relationshipName: String, actName: String?=null): RT {
//        return this.targetsAs<RndrMachine>(relationshipName).getAct(actName)
//    }

    // TODO: acts of a particular type should no longer be associated with particular RndrMachines
//    fun <T>getAct(actName: String?=null): T? {
//
//    }

    // TODO: NOTE - no override since takes a Trigger as arg... awkward?
//    fun trigger(runningTime:Double, properties: Map<String, Any?>) {
//        val act: T = this.actFactory(this)
//    }

}

fun createRndrMachine(single:Boolean=true, factory: (tr:Trigger)->Unit): RndrMachine {
    return RndrMachine().apply {
        this.single = single
        setFactory(factory)
        createMe() // TODO: should the create come before or after setFactory?
    }
}

