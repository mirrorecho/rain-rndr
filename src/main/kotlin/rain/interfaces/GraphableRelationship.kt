package rain.interfaces

interface GraphableRelationship: GraphableItem {

    val source: GraphableNode
    val target: GraphableNode

}