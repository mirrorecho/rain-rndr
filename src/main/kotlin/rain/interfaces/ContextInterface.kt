package rain.interfaces

interface ContextInterface {
    abstract fun graph(): GraphInterface

    abstract fun initEmptyGraph() // TODO: add arguments

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