package rain.language

import rain.interfaces.*

open class FancyNode(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context: ContextInterface = LocalContext,
):Node(key, properties, context) {

    var yo:String
        get() = this.properties["yo"] as String
        set(v) {this.properties["yo"]=v}

}