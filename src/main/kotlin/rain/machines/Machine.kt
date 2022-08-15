package rain.machines

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import rain.interfaces.*
import rain.language.*
import rain.patterns.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.Duration

interface Machine: LanguageNode {

    fun reset() { throw NotImplementedError() }

    fun render() { throw NotImplementedError() }


    // TODO float the best format for dur/delay/time!!????
    // ... and TODO should startDur even be here??
    abstract fun trigger(startDelay:Double, properties: Map<String, Any>)

}


open class Printer(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context: ContextInterface = LocalContext,
): Machine, Leaf(key, properties, context) {

    // TODO float the best format for dur/delay/time!!????
    // ... and TODO should startDur even be here??
    override fun trigger(startDelay:Double, properties: Map<String, Any>) {

        runBlocking {
            delay( (startDelay * 1000.0).roundToLong() )
        }
    }

}