package net.loute.freem.compiler.symbolTable.frontend.token

import net.loute.freem.compiler.util.indexMapOf

private val CONST_MAX_RANK = 1

private val constOperatorRankTable = indexMapOf(CONST_MAX_RANK) {
    commit (
        Token.Operator.D_STAR,
    )
    commit(
        Token.Operator.STAR,
        Token.Operator.SLASH,
        Token.Operator.PERCENT,
    )
    commit (
        Token.Operator.PLUS,
        Token.Operator.MINUS,
    )
    commit(
        Token.Operator.GREATER,
        Token.Operator.LESS,
        Token.Operator.GREATER_EQUAL,
        Token.Operator.LESS_EQUAL,
    )
    commit (
        Token.Operator.NOT_EQ,
        Token.Operator.D_EQUAL,
    )
    commit (
        Token.Operator.AND
    )
    commit (
        Token.Operator.OR
    )
}

private val CONST_MIN_RANK = constOperatorRankTable.toList().last().second

val Token.Operator.rank get() = constOperatorRankTable[this]?:0
val Token.Operator.Companion.MIN_RANK get() = CONST_MIN_RANK
val Token.Operator.Companion.MAX_RANK get() = CONST_MAX_RANK