import rain.interfaces.*
import rain.language.*
import rain.patterns.*
import rain.machines.*
import rain.rndr.*
import rain.utils.*

fun yoFancy(li:LanguageItem) {
    println(li::class.simpleName)
}


fun main() {

//    LocalContext.registerLabel(FancyNode.label)
    LocalContext.registerLabel(Relationship.label)

    LocalContext.registerLabel(Tree.label)
    LocalContext.registerLabel(CellTree.label)
    LocalContext.registerLabel(Cell.label)
    LocalContext.registerLabel(Cue.label)

    LocalContext.registerLabel(Contains.label)
    LocalContext.registerLabel(Cues.label)
    LocalContext.registerLabel(CuesFirst.label)
    LocalContext.registerLabel(CuesNext.label)
    LocalContext.registerLabel(CuesLast.label)

    LocalContext.registerLabel(Printer.label)
    LocalContext.registerLabel(Circle.label)



//    var f = LocalContext.make<FancyNode>("FancyNode", "NODEFN123", mapOf("yo" to "MAMA"))

//    val fn = FancyNode("NODE_FN", mapOf("yo" to "MAMA"))
//    fn.createMe()
//
//    val fn2 = FancyNode("NODE_FN2", mapOf("yo" to "YOYO"))
//    fn2.createMe()
//
//    val fn3 = FancyNode("NODE_FN3", mapOf("yo" to "YOYO"))
//    fn3.createMe()
//
//    val rel = Relationship(key="REL1", source_key="NODE_FN", target_key="NODE_FN2")
////    println(rel.source.key)
//    rel.createMe()
//
//    val fn_a = FancyNode("NODE_FN", )
//    fn_a.read()

//    val seq = Select(label="FancyNode", properties = mapOf("yo" to "YOYO"), )
//    seq.asTypedSequence<FancyNode>().forEach { println(it.key) }

//    fn.r(SelectDirection.RIGHT).n().forEach { println(it.key) }
//    Select(keys = listOf("NODE_FN")).r(SelectDirection.RIGHT).n().asSequence().forEach { println(it.key) }
//    println("-----------------------------------------------------------")
//    fn2.r(SelectDirection.LEFT).n().forEach {println(it.key) }


//    println(fn.r(SelectDirection.RIGHT).n().first?.key)
//    println(fn.selectSelf.targets().first?.key)

//    println("-----------------------------------------------------------")
    // create machines with machine defaults

    Circle("BIG_CIRCLE", mapOf("radius" to 200.0)).createMe()
    Circle("SMALL_CIRCLE", mapOf("radius" to 90.0)).createMe()

    println("-----------------------------------------------------------")
    // create the cell patterns that trigger the machines

    val c1 = Cell("C1",
//        mapOf("simultaneous" to true)
    )
    c1.dur = sequenceOf(4.0, 2.0, 1.0)
    c1.properties["fillH"] = sequenceOf(0.0, 200.0) // TODO: simplify as c1.setProperty("vHSV", 0.9, 0.4, 0.4)
    c1.machine = cycleOf("BIG_CIRCLE")
    c1.properties["name"] = cycleOf("C1")
    c1.properties["x"] = sequenceOf(0.1, 0.9, 0.4)
    c1.createMe()

    val c2 = Cell("C2")
    c2.dur = sequenceOf(3.0, 1.0, 0.5)
    c2.machine = cycleOf("SMALL_CIRCLE")
    c2.properties["name"] = cycleOf("C2")
    c2.createMe()

    println("-----------------------------------------------------------")
    // arrange the cell patterns into a piece

    val t1 = CellTree("T1")
    t1.createMe()
    t1.extend(c1, c2)

    val t2 = CellTree("T2")
    t2.createMe()
    t2.extend(c2, c1)

    val t = CellTree("T",
        mapOf("simultaneous" to true)
    )
    t.createMe()
    t.extend(t1, t2)

    println("-----------------------------------------------------------")

    println(t.simultaneous)

    println("-----------------------------------------------------------")

    val rndrMachines = Palette.fromKeys<Circle>("BIG_CIRCLE", "SMALL_CIRCLE")

    val player = RndrPlayer(t, rndrMachines as Palette<RndrMachine>)

    println("-----------------------------------------------------------")

    player.play()

//    fun yoDict(vararg pairs: Pair<String, Any?>): Map<String, Any?> = pairs.toMap()
//
//    yoDict("fo" to null, "fan" to 4)

//    val s = Select(keys=listOf("T", "T1", "T2")).toPalette<CellTree>()



//    println(t1.r(SelectDirection.RIGHT, "CUES_FIRST").first)

//    println(t1.branches.getBranchCues().toList())
//    println(t1.branches.getBranchCues().toList())
//    t.branches.forEach { println(it.key) }
//    t.nodes.forEach { println(it.key) }
//    t.leaves.forEach { println(it.key) }
//
//    t.veins.forEach { println(it) }

}