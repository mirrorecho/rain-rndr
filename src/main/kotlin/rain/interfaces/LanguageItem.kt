package rain.interfaces

interface LanguageItem: GraphableItem {

    // TODO include parent classes in label list
    override val labels: List<String> get() = listOf(this.primaryLabel)

    override val primaryLabel: String get() = this::class.simpleName ?: ""

    val context: ContextInterface

    val graph: GraphInterface get() = this.context.graph

    abstract fun save()

    abstract fun read()

    abstract fun delete()

    abstract fun mergeMe()

    abstract fun deleteMe()

}