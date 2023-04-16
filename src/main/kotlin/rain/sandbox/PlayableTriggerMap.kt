package rain.sandbox

// TODO / NOTES: base machine types:
//  - AnimValue - a simple value that can be animated, with optional easing/envelope
//  - AnimMulti - multiple values, which can either be fixed values, animated values (with optional easing/envelope),
//      or references to other AnimValue or AnimMulti machines

// TODO: how to associate dependant MachineFuncs to particular ops?
val triggersToPlay = mutableMapOf(
    0.0 to listOf(
        mapOf(
            "machine" to "OPERATE", // assume only 1 machine necessary for any given work?
            "key" to "SIZE_OP",
            "dur" to 4.0, // assume optional? (time could also just be when all associated machine durs are over?)
            "startAction" to 1, // 0=nothing, 1=start, 2=free, 3="pause" (op is not running, but still exists)
            // TODO consider whether endAction is best specified by the machine implementation (as opposed to the trigger)
            "endAction" to 0, // ditto values as startAction
        ),
        mapOf(
            "machine" to "OPERATE", // assume only 1 machine necessary for any given work?
            "key" to "SMALL_CIRCLE_OP",
            "dur" to 4.0, // assume optional? (time could also just be when all associated machine durs are over?)
            "startAction" to 1, // 0=nothing, 1=start, 2=free, 3="pause" (op is not running, but still exists)
            // TODO consider whether endAction is best specified by the machine implementation (as opposed to the trigger)
            "endAction" to 0, // ditto values as startAction
        ),
        mapOf(
            // MACHINE: kick off SIZE ... getting bigger for 2"
            "machine" to "SIZE", // (an AnimValue)
            "startValue" to 2.0,
            "endValue" to 40.0,
            "dur" to 2.0,
            "ops" to listOf("SIZE_OP"),
        ),
        mapOf(
            "machine" to "SMALL_CIRCLE", // (a Circle, which inherits from AnimMulti)
            "ops" to listOf("SIZE_OP", "SMALL_CIRCLE_OP"),
            // TODO: OK, but how to handle the case where multiple ops could would have values
            //  with the same names? (e.g. values for HSVa for fill vs stroke)

            "SIZE" to 1001, // NOTE: this is confusing (i.e. not clear whether value is the ID of the op or the actual size value)
            "op" to 1002,
            // "HEIGHT" to 1002, // NOTE... this is redundant / assumed
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
            "doneAction" to 2,
        ),
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "op" to 1003,
            // "HEIGHT" to 1003, // NOTE... this is redundant / assumed
            "gate" to true,
            "dur" to 4.0,
            "doneAction" to 2,
        ),
        mapOf(
            "machine" to "SIZE",
            "op" to 1002,
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
            // 0=nothing, 1="pause" (op is not running, but still exists), 2=free
            "doneAction" to 2,
        ),
    ),
    1.0 to listOf(
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
        ),
    ),
    1.4 to listOf(
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
        ),
    ),
    2.0 to listOf(
        mapOf(
            // now SIZE gets slightly smaller for the 2nd 2"
            "machine" to "SIZE",
            "startValue" to 40.0, // redundant?
            "endValue" to 20.0,

            // this is the operation of the machine
            "op" to 1001,
            "dur" to 2.0,
            //"startAction" to 0 // not needed, since 0 is the default
            "endAction" to 2 // free
        ),
    ),
    4.0 to listOf(
        mapOf(
            "machine" to "SMALL_CIRCLE",
            "gate" to true, // type: Boolean? (true means gate on, false means gate off, null means no change)
            "dur" to 4.0,
        ),
    ),
)