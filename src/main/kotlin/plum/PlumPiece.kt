package plum

import rain.language.*
import rain.patterns.*
import rain.machines.*
import rain.rndr.*
import rain.utils.cycleOf


fun main() {

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
    LocalContext.registerLabel(Line.label)
    LocalContext.registerLabel(Rectangle.label)
    LocalContext.registerLabel(Text.label)

    LocalContext.registerLabel(LineSwirl.label)

//    Circle("BIG_CIRCLE", mapOf("radius" to 10.0, "x" to 1.0, "y" to 0.0)).createMe()
//    Circle("SMALL_CIRCLE", mapOf("radius" to 2.0)).createMe()
//    Rectangle("RECT1").createMe()

    Line("LINE1").createMe()
    Text("TEXT_MACHINE1").createMe()
    LineSwirl("LINE_SWIRL").createMe()

    println("-----------------------------------------------------------")
    // create the cell patterns that trigger the machines


    val line_text = Cell("LINE_TEXT",
//        mapOf("simultaneous" to true)
    )
    line_text.setVeinCycle("name", "LINE_TEXT1") // TODO: is this being used?
    line_text.setVeinCycle("machine", "TEXT_MACHINE1")
    line_text.setVeinCycle("text", "RADISH")
    line_text.setVeins("dur", 0.0, 1.0, 0.5, 1.0)
    line_text.setVeins("x", 1.0, 0.9, 0.4, 1.0)
    line_text.setVeins("y", 1.0, 0.1, 0.1, 1.0)
    line_text.setVeinCycle("strokeH", 90.0)
    line_text.setVeinCycle("strokeS", 1.0)
    line_text.setVeinCycle("strokeV", 0.6)
    line_text.setVeinCycle("strokeA", 0.9)
    line_text.setVeinCycle("fillH", 90.0)
    line_text.setVeinCycle("fillS", 1.0)
    line_text.setVeinCycle("fillV", 0.6)
    line_text.setVeinCycle("fillA", 0.9)
    line_text.setVeins("gate", true, true, true, false)
//    c1.setVeins("fillH", 0.0, 200.0, 20.0)
    line_text.createMe()

    val l1 = CellTree("L1")
    l1.createMe()
    l1.properties["text"] = cycleOf("YOYO")
    l1.extend(line_text, line_text)

    fun makeSwirl(name:String): Cell {
        val swirl = Cell().apply {
            setVeinCycle("machine", "LINE_SWIRL")
            setVeinCycle("name", name)
            setVeins("dur", 0.0, 0.4, 0.2)
            setVeins("gate", true, true, false)
            setVeins("x", 0.0,0.0,0.8)
            setVeins("x2", 0.4,0.6,1.0)
            setVeins("y", 0.4,0.0,0.3)
            setVeins("y2", 0.6,1.0,0.7)
            setVeinCycle("strokeH", 40.0)
            setVeinCycle("strokeS", 0.6)
            setVeinCycle("strokeV", 0.8)
            setVeins("strokeA", 0.0, 0.8, 0.0)
            setVeinCycle("fillH", 90.0)
            setVeinCycle("fillS", 1.0)
            setVeinCycle("fillV", 0.6)
            setVeinCycle("fillA", 0.9)

            setVeins("strokeWeight", 0.0, 2.0, 0.0)
        }
        swirl.createMe()
        return swirl
    }


    val t = CellTree("T",
//        mapOf("simultaneous" to true)
    )
    t.simultaneous = true
    t.createMe()
    println(t.properties)
    (0 ..9).forEach {
        t.extend(makeSwirl("SWIRL"+it.toString()))
    }

    val tr = CellTree()
    tr.createMe()
    tr.extend(t, t, t, t)


//    val c2 = Cell("C2")
//    c2.setVeinCycle("name", "C2") // TODO: is this being used?
//    c2.setVeinCycle("machine", "TEXT1")
//    c2.setVeinCycle("text", "Yo Mama")
//    c2.setVeins("dur", 2.0)
//    c2.createMe()
//
//    println("-----------------------------------------------------------")
//    // arrange the cell patterns into a piece
//
//    val t1 = CellTree("T1")
//    t1.createMe()
//    t1.extend(c1, c2)
//
//    val t2 = CellTree("T2")
//    t2.createMe()
//    t2.extend(c2, c1)
//
//    val t = CellTree("T",
//        mapOf("simultaneous" to true)
//    )
//    t.createMe()
//    t.extend(t1, t2)
//

    val rndrMachines = Palette.fromKeys<RndrMachine>("LINE1", "TEXT_MACHINE1", "LINE_SWIRL")

    val player = RndrPlayer(tr, rndrMachines)

    println("-----------------------------------------------------------")

    player.play()


}