package rain.interfaces

interface LabelInterface {
    val labels: String
    val primary: String
    fun createItem(context:ContextInterface, key:String,
                   properties:Map<String, Any> =mapOf()): GraphableItem
}