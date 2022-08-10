import org.openrndr.application
import rain.*
import rain.interfaces.*
import rain.language.*
import rain.patterns.*
import rain.utils.*

fun yoFancy(li:LanguageItem) {
    println(li::class.simpleName)
}


fun main() {

    LocalContext.registerLabel(FancyNode.label)
    LocalContext.registerLabel(Relationship.label)

//    var f = LocalContext.make<FancyNode>("FancyNode", "NODEFN123", mapOf("yo" to "MAMA"))

    val fn = FancyNode("NODE_FN", mapOf("yo" to "MAMA"))
    fn.createMe()

    val fn2 = FancyNode("NODE_FN2", mapOf("yo" to "YOYO"))
    fn2.createMe()

    val fn3 = FancyNode("NODE_FN3", mapOf("yo" to "YOYO"))
    fn3.createMe()

    val rel = Relationship(key="REL1", source_key="NODE_FN", target_key="NODE_FN2")
//    println(rel.source.key)
    rel.createMe()

    val fn_a = FancyNode("NODE_FN", )
    fn_a.read()

//    val seq = Select(label="FancyNode", properties = mapOf("yo" to "YOYO"), )
//    seq.asTypedSequence<FancyNode>().forEach { println(it.key) }
    println("-----------------------------------------------------------")
    fn.r(SelectDirection.RIGHT).n().asSequence().forEach {println(it.key) }
//    println("-----------------------------------------------------------")
//    fn2.r(SelectDirection.LEFT).n().asSequence().forEach {println(it.key) }

//    yoFancy(fn2)
    println("-----------------------------------------------------------")

//    val c = Cell()
//    c.dur = sequenceOf(1,2,4)
//    c.machine = cycleOf("FLUTE")
//
//    c.veins.forEach { println(it) }

}