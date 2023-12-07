package rain.rndr

class Trigger<T:Act>(
    val score: Score,
    val rndrMachine: RndrMachine<T>,
    val act:T? = null, // passed if triggering an existing act
    val runningTime:Double = 0.0, // TODO: used?
    val properties: Map<String, Any?>
) {
    fun propertyAsValueAct(propertyName:String, actName:String?=null):ValueAct {
        return if (actName.isNullOrBlank()) ValueAct(value=propertyAs(propertyName)) else ValueAct(name=actName, value=propertyAs(propertyName))
    }

    fun <P>propertyAs(propertyName:String):P {
        return properties[propertyName] as P
    }
}