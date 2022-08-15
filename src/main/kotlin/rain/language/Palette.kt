package rain.language

import rain.interfaces.*

class Palette<T: LanguageNode>(
    vararg nodes: T,
    val context: ContextInterface = LocalContext
) {
    init { this.extend(*nodes) }

    companion object {
        fun <S:Node>fromSelect(select: SelectInterface): Palette<S> {
            val myPalette = Palette<S>()
            myPalette.extendBySelect(select)
            return myPalette
        }

        fun <S:Node>fromKeys(vararg keys: String): Palette<S> {
            val myPalette = Palette<S>()
            myPalette.extendByKeys(*keys)
            return myPalette
        }

    }

    val graph: GraphInterface get() = this.context.graph

    private val nodes: MutableMap<String, T> = mutableMapOf()

    fun extend(vararg nodes: T) {
        nodes.forEach { this.nodes[it.key] = it }
    }

    fun extendByKeys(vararg keys: String) {
        this.extendBySelect(Select(context = this.context, keys = keys.toList()))
    }

    fun extendBySelect(select:SelectInterface) {
        select.asTypedSequence<T>().forEach { this.nodes[it.key]=it }
    }

    // TODO maybe: implement below?
    // fun extendByKey(vararg keys: String)

    fun put(node: T) {this.nodes[node.key]=node}

    operator fun get(key: String): T = this.nodes[key]!!

    operator fun set(key: String, value: T) {this.nodes[key]=value}

}