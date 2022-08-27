package rain.patterns

import rain.interfaces.*
import rain.language.*

// TODO... long and nasty with all the class inheritance and companion objects ... REFACTOR!!!!

open class Cue(
    key:String = rain.utils.autoKey(),
    properties: Map<String, Any?> = mapOf(),
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
    properties: Map<String, Any?> = mapOf(),
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
    properties: Map<String, Any?> = mapOf(),
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

open class CuesNext(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any?> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<CuesNext> = Label(
            factory = { k, p, c -> CuesNext(k, null, null, p, c) },
            labels = listOf("CUES_NEXT"),
        )
    }

    override val label: LabelInterface get() = CuesNext.label
}

// ===========================================================================================================

open class CuesFirst(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any?> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<CuesFirst> = Label(
            factory = { k, p, c -> CuesFirst(k, null, null, p, c) },
            labels = listOf("CUES_FIRST"),
        )
    }

    override val label: LabelInterface get() = CuesFirst.label
}

// ===========================================================================================================

open class CuesLast(
    key:String = rain.utils.autoKey(),
    source_key: String?, // TODO: this is wonky... really, key string should be required
    target_key: String?, // ditto
    properties: Map<String, Any?> = mapOf(),
    context:ContextInterface = LocalContext,
): Relationship(key, source_key, target_key, properties, context) {

    companion object : ItemCompanion() {
        override val label: Label<CuesLast> = Label(
            factory = { k, p, c -> CuesLast(k, null, null, p, c) },
            labels = listOf("CUES_LAST"),
        )
    }

    override val label: LabelInterface get() = CuesLast.label
}


