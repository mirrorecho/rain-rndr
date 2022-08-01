package rain.interfaces

enum class SelectDirection(val shortForm: String) {
    RIGHT("->"),
    LEFT("<-"),
    RIGHT_NODE("->()"),
    LEFT_NODE("<-()")
}