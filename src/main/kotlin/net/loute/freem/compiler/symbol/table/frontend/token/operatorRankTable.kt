package net.loute.freem.compiler.symbol.table.frontend.token

import net.loute.freem.compiler.util.indexMapOf

private val CONST_MAX_RANK = 1

/*
private val constOperatorRankTable = indexMapOf(CONST_MAX_RANK) {
    commit (
        Token.Operator("**"),
    )
    commit(
        Token.Operator("*"),
        Token.Operator("/"),
        Token.Operator("%"),
    )
    commit (
        Token.Operator("+"),
        Token.Operator("-"),
    )
    commit(
        Token.Operator(">"),
        Token.Operator("<"),
        Token.Operator(">="),
        Token.Operator("<="),
    )
    commit (
        Token.Operator("!="),
        Token.Operator("=="),
    )
    commit (
        Token.Operator("&&"),
    )
    commit (
        Token.Operator("||"),
    )
}

private val CONST_MIN_RANK = constOperatorRankTable.toList().last().second

val Token.Operator.rank get() = constOperatorRankTable[this]?:0
val Token.Operator.Companion.MIN_RANK get() = CONST_MIN_RANK
val Token.Operator.Companion.MAX_RANK get() = CONST_MAX_RANK
*/