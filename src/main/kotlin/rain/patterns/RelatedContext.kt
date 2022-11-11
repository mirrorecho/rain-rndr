package rain.patterns

// HAD CONSIDERED: maybe this should just a live on the Cue itself?
// OR MAYBE NOT, because that doesn't take into account the ancestors
// WHERE SHOULD THIS LIVE????

class RelatedContext(
    val cue: Cue,
    val ancestors: List<Pattern> // trees or alters
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