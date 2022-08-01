package rain.interfaces

import rain.language.LocalContext

interface LabelInterface {
    val labels: List<String>
    val primary: String
//    val factory: (context:ContextInterface, key:String, properties:Map<String, Any>)-> LanguageItem
    val factory: (key:String, properties:Map<String, Any>, context:ContextInterface)-> LanguageItem
    val context:ContextInterface

    fun make(key:String, properties:Map<String, Any> = mapOf()): LanguageItem {
        return this.factory(key,properties,this.context)
    }

    // TODO: needed?
//    fun createItem(context:ContextInterface, key:String, properties:Map<String, Any> =mapOf()): LanguageItem {
//        return this.factory(context, key, properties)
//    }
}