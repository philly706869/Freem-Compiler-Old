package net.loute.freem.compiler.symbolTable.front.token.table.operator

import net.loute.freem.compiler.symbolTable.front.token.Token

private val constOperatorRankTable = setOf<Set<Token.Type.Operator>>(
    setOf(
        Token.Type.Operator.D_STAR,
    ),
    setOf(
        Token.Type.Operator.STAR,
        Token.Type.Operator.SLASH,
        Token.Type.Operator.PERCENT,
    ),
    setOf(
        Token.Type.Operator.PLUS,
        Token.Type.Operator.MINUS,
    ),
    setOf(
        Token.Type.Operator.GREATER,
        Token.Type.Operator.LESS,
        Token.Type.Operator.GREATER_EQUAL,
        Token.Type.Operator.LESS_EQUAL,
    ),
    setOf(
        Token.Type.Operator.NOT_EQ,
        Token.Type.Operator.D_EQUAL,
    ),
    setOf(
        Token.Type.Operator.AND,
    ),
    setOf(
        Token.Type.Operator.OR,
    ),
    setOf(
        Token.Type.Operator.LEFT_PAREN,
        Token.Type.Operator.RIGHT_PAREN,
    )
)

val Token.Type.Operator.Companion.rankTable get() = constOperatorRankTable
fun Token.Type.Operator.getRank() = Token.Type.Operator.rankTable.run { indexOf(find { it.any { item -> item == this@getRank } }) }