package rain.patterns

import rain.interfaces.*
import rain.language.*

// TODO... long and nasty with all the class inheritance and companion objects ... REFACTOR!!!!

open class Cue(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any> = mapOf(),
    context: ContextInterface = LocalContext,
): Node(key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Cue> = Label(
            factory = { k, p, c -> Cue(k, p, c) },
            labels = listOf("Cue"),
        )
    }
    override val label: LabelInterface get() = Cue.label

    val cuesPattern: Pattern get() = r(SelectDirection.RIGHT, "CUES").n().first as Pattern
//    # # TO CONSIDER: would this be used?
//    # if alter_node := self.altered_by:
//    #     return alter_node.alter(pattern)
//    # else:
//    #     return pattern
//
//    # # TO CONSIDER: would this be used?
//    # @property
//    # def altered_by(self) -> Tuple["rain.AlterCue"]:
//    #     return tuple(self.r("<-", "ALTERS").n())
}

// ===========================================================================================================

open class Contains(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Contains> = Label(
            factory = { k, p, c -> Contains(k, null, null, p, c) },
            labels = listOf("CONTAINS"),
        )
    }

    override val label: LabelInterface get() = Contains.label
}

// ===========================================================================================================

open class Cues(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<Cues> = Label(
            factory = { k, p, c -> Cues(k, null, null, p, c) },
            labels = listOf("CUES"),
        )
    }

    override val label: LabelInterface get() = Cues.label
}

// ===========================================================================================================

open class CueNext(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<CueNext> = Label(
            factory = { k, p, c -> CueNext(k, null, null, p, c) },
            labels = listOf("CUE_NEXT"),
        )
    }

    override val label: LabelInterface get() = CueNext.label
}

// ===========================================================================================================

open class CueFirst(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<CueFirst> = Label(
            factory = { k, p, c -> CueFirst(k, null, null, p, c) },
            labels = listOf("CUE_FIRST"),
        )
    }

    override val label: LabelInterface get() = CueFirst.label
}

// ===========================================================================================================

open class CueLast(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<CueLast> = Label(
            factory = { k, p, c -> CueLast(k, null, null, p, c) },
            labels = listOf("CUE_LAST"),
        )
    }

    override val label: LabelInterface get() = CueLast.label
}


