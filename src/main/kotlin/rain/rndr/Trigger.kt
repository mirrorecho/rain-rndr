package rain.rndr

class Trigger<T:Act>(
    val score: Score,
    val rndrMachine: RndrMachine<T>,
    val act:T? = null, // passed if triggering an existing act
    val runningTime:Double = 0.0, // TODO: used?
    val properties: Map<String, Any?>
    // TODO... the trigger is what will need to connect an Act to its related/sub acts...
    //  ... (properties enough to implement through?)
) {

    // TODO: maybe remove this... in order to avoid the complexity of cascading triggers/acts?
    fun propertyAsValueAct(propertyName:String, actName:String?=null):ValueAct {
        return if (actName.isNullOrBlank()) ValueAct(value=propertyAs(propertyName)) else ValueAct(name=actName, value=propertyAs(propertyName))
    }

    fun <RA:Act>relatedAct(relationshipName: String, actName: String?=null): RA {
        return rndrMachine.getRelatedAct(relationshipName, actName)
    }

    val dur: Double by this.properties

    fun <P>propertyAs(propertyName:String):P {
        return properties[propertyName] as P
    }

}