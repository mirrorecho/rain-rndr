package rain.interfaces

import org.openrndr.Program
import rain.language.Relationship
import rain.rndr.MachineFuncOp

interface ContextInterface {
    val graph: GraphInterface


    // note, with Kotlin generics and class values, no need for initEmptyGraph and such nonsense
    // as in python implementation

    fun registerLabel(label:LabelInterface)

    fun get(labelName:String): LabelInterface?

    fun <T: LanguageItem>make(labelName:String, key:String, properties: Map<String, Any?> = mapOf(), context: ContextInterface=this): T

    fun <T: LanguageItem>make(fromItem:GraphableItem): T {
        return this.make(fromItem.primaryLabel, fromItem.key, fromItem.properties, this)
    }

    // TODO: this is odd, esp. since generic type is just LanguageItem (not a type of relationship)
    fun <T: LanguageItem>makeRelationship(labelName:String, key:String, source_key:String, target_key:String, properties: Map<String, Any?> = mapOf(), context: ContextInterface=this): T {
        val myRelationship = this.make(labelName, key, properties, context) as Relationship
        myRelationship.source_key = source_key
        myRelationship.target_key = target_key
        return myRelationship as T
    }

    fun <T: LanguageItem>makeRelationship(fromItem:GraphableRelationship): T {
        return this.makeRelationship(fromItem.primaryLabel, fromItem.key, fromItem.source.key, fromItem.target.key, fromItem.properties)
    }

    // Ops interface
    fun getOp(machineKey: String, opKey:String): MachineFuncOp?

    fun stopOp(machineKey: String, opKey:String)

    fun cycleOps()

}

//@property
//@abstractmethod
//def graph(self) -> GraphInterface: pass
//
//@abstractmethod
//def init_empty_graph(self, graph_type:type=None, **kwargs) -> GraphInterface: pass
//
//def init_graph(self, graph_type:type=None, **kwargs)-> GraphInterface:
//# here, this merely calls init_empty_graph to create a new empty graph
//# but specific implementations may connect to existing graph data stores
//return self.init_empty_graph(graph_type, **kwargs)
//
//# TO CONSIDER... a decorator for this
//@abstractmethod
//def register_types(self, *types): pass #TODO: type hinting for this
//
//@abstractmethod
//def get_type(self, label:str) -> type: pass
//
//@abstractmethod
//def new_by_key(self, key:str) -> GraphableInterface: pass
//
//def new_by_label_and_key(self, label:str, key:str, **kwargs) -> GraphableInterface:
//return self.get_type(label)(key, **kwargs)