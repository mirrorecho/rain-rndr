package rain.language

import rain.interfaces.*

open class Select(
    override val context:ContextInterface = LocalContext,
    override val label: String? = null,
    override val keys: List<String>? = null,
    override val properties: Map<String, Any>? = null,
    override val selectFrom: SelectInterface? = null,
    override val direction: SelectDirection? = null,
):SelectInterface {

    override fun get(key:String) {throw NotImplementedError()}

    override operator fun invoke(label:String?, keys:List<String>?, properties:Map<String,Any>?): Select {
        // TODO: should direction be set to this.direction? (warrants more thought and testing)
        return Select(context=this.context, label=label, keys=keys, properties=properties, selectFrom=this)
    }

    // TODO any way to avoid this duplicative implementation with default Type
    open fun r(direction:SelectDirection, label:String?, keys:List<String>?, properties:Map<String,Any>?):TargetedRelationshipSelect{
        return TargetedRelationshipSelect(context=this.context, label=label, keys=keys, properties=properties, selectFrom=this, direction=direction)
    }

     // TODO ditto, any way to avoid this duplicative implementation with default Type
    open fun n(label:String?, keys:List<String>?, properties:Map<String,Any>?):Select {
        throw NotImplementedError()
    }
}

class TargetedRelationshipSelect(
    context:ContextInterface = LocalContext,
    label: String? = null,
    keys: List<String>? = null,
    properties: Map<String, Any>? = null,
    selectFrom: SelectInterface? = null,
    direction: SelectDirection? = null,
):Select(context, label, keys, properties, selectFrom, direction) {
    // TODO: document
    // TODO: consider testing for supported directions (-> and <- only)

    override fun r(direction:SelectDirection, label:String?, keys:List<String>?, properties:Map<String,Any>?):TargetedRelationshipSelect {
        throw NotImplementedError("Chaining .r relationship selects not supported (add a .n select between)")
    }

    override fun n(label:String?, keys:List<String>?, properties:Map<String,Any>?):Select {
        return Select(context=this.context, label=label, keys=keys, properties=properties, selectFrom=this)
    }

}

// NOTE: implementation possibility below with type parameter on the class itself... something to consider

//open class Select<T:Item>(
//    override val context:ContextInterface = LocalContext,
//    override val label: String? = null,
//    override val keys: List<String>? = null,
//    override val properties: Map<String, Any>? = null,
//    override val selectFrom: SelectInterface? = null,
//    override val direction: SelectDirection? = null,
//):SelectInterface {
//
//    override fun get(key:String) {throw NotImplementedError()}
//
//    override operator fun invoke(label:String?, keys:List<String>?, properties:Map<String,Any>?): Select<T> {
//        // TODO: should direction be set to this.direction? (warrants more thought and testing)
//        return Select(context=this.context, label=label, keys=keys, properties=properties, selectFrom=this)
//    }
//
//    open fun <R:Relationship>rt(direction:SelectDirection, label:String?, keys:List<String>?, properties:Map<String,Any>?):TargetedRelationshipSelect<R> {
//        return TargetedRelationshipSelect(context=this.context, label=label, keys=keys, properties=properties, selectFrom=this, direction=direction)
//    }
//
//    // TODO any way to avoid this duplicative implementation with default Type
//    open fun r(direction:SelectDirection, label:String?, keys:List<String>?, properties:Map<String,Any>?):TargetedRelationshipSelect<Relationship> {
//        return this.rt(direction=direction, label=label, keys=keys, properties=properties)
//    }
//
//    open fun <N:Node>nt(label:String?, keys:List<String>?, properties:Map<String,Any>?):Select<N> {
//        throw NotImplementedError()
//    }
//
//    // TODO ditto, any way to avoid this duplicative implementation with default Type
//    open fun n(label:String?, keys:List<String>?, properties:Map<String,Any>?):Select<Node> {
//        return this.nt(label=label, keys=keys, properties=properties)
//    }
//}
//
//class TargetedRelationshipSelect<R:Relationship>(
//    context:ContextInterface = LocalContext,
//    label: String? = null,
//    keys: List<String>? = null,
//    properties: Map<String, Any>? = null,
//    selectFrom: SelectInterface? = null,
//    direction: SelectDirection? = null,
//):Select<R>(context, label, keys, properties, selectFrom, direction) {
//    // TODO: document
//    // TODO: consider testing for supported directions (-> and <- only)
//
//    override fun <R:Relationship>rt(direction:SelectDirection, label:String?, keys:List<String>?, properties:Map<String,Any>?):TargetedRelationshipSelect<R> {
//        throw NotImplementedError("Chaining .r relationship selects not supported (add a .n select between)")
//    }
//
//    override fun <N:Node>nt(label:String?, keys:List<String>?, properties:Map<String,Any>?):Select<N> {
//        return Select(context=this.context, label=label, keys=keys, properties=properties, selectFrom=this)
//    }
//
//}
