package rain.language

import rain.interfaces.*

open class Context<G:GraphInterface>(
    override val graph: G
): ContextInterface {

    private val labelRegistry: MutableMap<String, LabelInterface> = mutableMapOf()

    override fun registerLabel(label:LabelInterface) {
        this.labelRegistry[label.primary] = label
    }

    override fun get(labelName:String): LabelInterface? = this.labelRegistry[labelName]

    // TODO: should this be defined in the interface
    fun <T: LanguageItem>make(labelName:String, key:String, properties:Map<String, Any> = mapOf()): T {
        return this.labelRegistry[labelName]?.make(key, properties) as T
    }

}