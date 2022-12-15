package net.loute.freem.compiler.symbolTable.frontend.token

/**
 * index: operator rank
 */
private val constOperatorRankTable = setOf<Set<Token.Operator>>(
    setOf(
        Token.Operator.D_STAR,
    ),
    setOf(
        Token.Operator.STAR,
        Token.Operator.SLASH,
        Token.Operator.PERCENT,
    ),
    setOf(
        Token.Operator.PLUS,
        Token.Operator.MINUS,
    ),
    setOf(
        Token.Operator.GREATER,
        Token.Operator.LESS,
        Token.Operator.GREATER_EQUAL,
        Token.Operator.LESS_EQUAL,
    ),
    setOf(
        Token.Operator.NOT_EQ,
        Token.Operator.D_EQUAL,
    ),
    setOf(
        Token.Operator.AND,
    ),
    setOf(
        Token.Operator.OR,
    ),
    setOf(
        Token.Operator.LEFT_PAREN,
    ),
)

val Token.Operator.Companion.rankTable get() = constOperatorRankTable
fun Token.Operator.getRank() = Token.Operator.rankTable.run { indexOf(find { it.any { item -> item == this@getRank } }) + 1 }