package rain.interfaces

interface LanguageItem: GraphableItem {

    val label: LabelInterface

    // TODO include parent classes in label list
    override val labels: List<String> get() = this.label.labels

    override val primaryLabel: String get() = this.label.primary

    val context: ContextInterface

    val graph: GraphInterface get() = this.context.graph

    fun save(): GraphableItem

    fun read(): GraphableItem

    fun delete()

    fun mergeMe(): GraphableItem

    fun createMe(): GraphableItem

}