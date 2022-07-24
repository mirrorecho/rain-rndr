package rain.utils

import kotlin.math.abs

fun autoKey(): String {
    // TODO: consider a better implementation for this?
    return  abs((100..999999999999).random()).toString()
}