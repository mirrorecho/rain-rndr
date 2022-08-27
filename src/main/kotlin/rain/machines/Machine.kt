package rain.machines

import rain.interfaces.*
import rain.language.*
import rain.patterns.*

interface Machine: LanguageNode {

    fun reset() { throw NotImplementedError() }

    fun render() { throw NotImplementedError() }

    // TODO maybe use playerContext object (instead of just runningTime)
    fun trigger(runningTime:Double, properties: Map<String, Any?>) { throw NotImplementedError() }

}


open class Printer(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Printer> = Label(
            factory = { k, p, c -> Printer(k, p, c) },
            labels = listOf("Printer", "Machine"),
        )
    }

    override val label: LabelInterface get() = Printer.label

    override fun trigger(runningTime:Double, properties: Map<String, Any?>) {
        println("$runningTime: " + this.key + " " + properties.toString())
    }

}