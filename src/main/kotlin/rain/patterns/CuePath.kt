package rain.patterns

// HAD CONSIDERED: maybe this should just a live on the Cue itself?
// OR MAYBE NOT, because that doesn't take into account the ancestors
// WHERE SHOULD THIS LIVE????

class CuePath(
    val cue: Cue, // needed? (probably not, but also probably simplifieds things
    // TODO: which of the following make more sense?
    val ancestors: List<Pattern>, // trees or alters
//    val ancestorCues: List<Cue> // needed?
) {

    val aunts = listOf<Pattern>()
    val preceding = listOf<Pattern>()
    val following = listOf<Pattern>()
    val siblings = listOf<Pattern>()

    val parent: Pattern? = null
    val previous: Pattern? = null
    val next: Pattern? = null

    fun yo() {
        this.following
    }

}