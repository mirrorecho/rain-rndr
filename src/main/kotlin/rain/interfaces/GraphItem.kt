package rain.interfaces

import rain.graph.Graph

interface GraphItem: GraphableItem {

    fun cleanup(graph: Graph) {}

}