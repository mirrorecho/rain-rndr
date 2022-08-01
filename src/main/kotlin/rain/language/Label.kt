package rain.language

import rain.interfaces.*
import kotlin.reflect.KClass

// the POINT of a Label:
// MOST IMPORTANT: a factory for a type of language item ->
// ... given data for the item (key, properties), instantiates that item

class Label<T:LanguageItem>(
    override val factory: (key:String, properties:Map<String, Any>, context:ContextInterface)-> T,
    override var labels: List<String> = LanguageItem::class.supertypes.map { it.toString() }, // TODO set this automatically based on T
    override val context:ContextInterface = LocalContext,
//    var factory2: (key:String, properties:Map<String, Any>)-> T
):LabelInterface {
    override  val primary = labels[0]
//    var myType: KClass<T> = Item

//    inline fun <reified L:T> setup() {
//        this.labels = L::class.supertypes.map { it.toString() }
//        val t = L::class
//        this.factory = {k,p -> t.inv(k,p)}
//    }

    init {
        context.registerLabel(this)
    }

    override fun make(key:String, properties:Map<String, Any>): T {
        return this.factory(key,properties,this.context)
    }

}



