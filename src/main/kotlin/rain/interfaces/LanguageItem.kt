package rain.interfaces

import rain.language.Item
import rain.language.TargetedRelationshipSelect

interface LanguageItem: GraphableItem {

    val label: LabelInterface

    // TODO include parent classes in label list
    override val labels: List<String> get() = this.label.labels

    override val primaryLabel: String get() = this.label.primary

    val context: ContextInterface

    val graph: GraphInterface get() = this.context.graph

    val selectSelf: SelectInterface

    fun save(): LanguageItem {
        context.graph.save(this)
        return this
    }

    fun read(): LanguageItem {
        context.graph.read(this)
        return this
    }

    fun delete() {
        context.graph.delete(this.key)
    }

    fun mergeMe(): LanguageItem {
        context.graph.merge(this)
        return this
    }

    fun createMe(): LanguageItem {
        context.graph.create(this)
        return this
    }

    fun <T>getAs(n:String) = this[n] as T

}

// ===========================================================================================================

interface LanguageNode: LanguageItem, GraphableNode {
    fun r(direction: SelectDirection, label:String?=null, keys:List<String>?=null, properties:Map<String,Any>?=null): SelectInterface

    // TODO: is this even necessary in this interface? Or just include in the Node class?
    fun <T:LanguageNode?>targetsAs(label:String?=null, keys:List<String>?=null, properties:Map<String,Any>?=null): T
}

// ===========================================================================================================

interface LanguageRelationship: LanguageItem, GraphableRelationship {

}