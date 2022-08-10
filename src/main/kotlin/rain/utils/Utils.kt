package rain.utils

import kotlin.math.abs

fun autoKey(): String {
    // TODO: consider a better implementation for this?
    return  abs((100..999999999999).random()).toString()
}

// slick way to create infinite cycle:
// https://stackoverflow.com/questions/48007311/how-do-i-infinitely-repeat-a-sequence-in-kotlin
fun <T> Sequence<T>.cycle() = sequence { while (true) yieldAll(this@cycle) }

fun <T>cycleOf(vararg elements:T): Sequence<T> = elements.asSequence().cycle()