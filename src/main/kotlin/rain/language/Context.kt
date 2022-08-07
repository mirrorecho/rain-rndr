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

    override fun <T: LanguageItem>make(labelName:String, key:String, properties:Map<String, Any>, context:ContextInterface): T {
        return this.labelRegistry[labelName]?.make(key, properties, context) as T
    }

}