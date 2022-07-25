package rain.interfaces

interface SelectInterface {

    val graph: GraphInterface

    val context: ContextInterface

    val label: String?

    val keys: List<String>?

    val properties: Map<String, Any>

    val selectFrom: SelectInterface?

    fun asSequence(): Sequence<LanguageItem> = sequence {
        yieldAll(this@SelectInterface.graph.selectInterface)
    }

    // TODO maybe: could use for more general implementation
//    fun indexOfFirst(key:String, predicate: (LanguageItem)-> Boolean ): Int {
//        return this.asSequence().indexOfFirst(predicate)
//    }

    fun indexOfFirst(key:String ): Int = this.asSequence().indexOfFirst {it.key==key}

    // TODO: ever used?
    fun contains(key: String): Boolean = this.indexOfFirst(key) > -1

    val first: LanguageItem get() = this.asSequence().first()

    fun getItem(key: String) {throw NotImplementedError()}

    // same as implementing __call__ method in python... cool
    operator fun invoke(label:String, keys:List<String>, properties:Map<String,Any>)

}