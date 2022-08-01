import org.openrndr.application
import rain.*
import rain.graph.Graph
import rain.interfaces.*
import rain.language.FancyNode
import rain.language.Label
import rain.language.LocalContext

class FancyNode2(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
):FancyNode(key, properties, context) {

    companion object {
        val label: Label<FancyNode2> = Label(
            factory = {k,p,c -> FancyNode2(k,p,c)},
            labels = listOf("FancyNode2", "FancyNode"),
            context = LocalContext
        )
//        abstract val label: LabelInterface< Item >
//        fun make(key: String = rain.utils.autoKey()): Item {
//            return Item(key)
//        }
    }

}

fun yoFancy(li:LanguageItem) {
    println(li::class.simpleName)
}


fun main() {
    var graph = Graph()
//    var fn2 = FancyNode2()
    println(LocalContext.get("FancyNode2"))
//    var
//    var fn2 = LocalContext.make<FancyNode2>("FancyNode2", "NODEFN123")

//    var l = Label<FancyNode>(
//        { o1, er, df
//            return FancyNode()
//        }
//    )

    println(graph)
//    yoFancy(fn2)
    println("-----------------------------------------------------------")


}