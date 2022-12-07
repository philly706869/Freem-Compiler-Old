package net.loute.freem.compiler.symbolTable.front

import net.loute.freem.compiler.FreemCompiler
import net.loute.freem.compiler.symbolTable.front.token.Token
import net.loute.freem.compiler.symbolTable.front.token.getRank
import net.loute.freem.compiler.symbolTable.raiseCompileError
import java.util.*
import kotlin.collections.ArrayList

//fun String.trimEdge() = substring(1 until length - 1)

object Parser {
    class SyntaxTree
    fun parseAnalyse(tokenArray: Collection<Token>) = SyntaxTree().apply {

    }

    fun shuntingYardWith(tokens: FreemCompiler.TokenArray): FreemCompiler.TokenArray {
        val output = FreemCompiler.TokenArray()
        val operatorStack = Stack<Token.Operator>()

        tokens.forEach {
            when (it) {
                is Token.PolymorphicToken -> output.add(it)
                Token.Operator.LEFT_PAREN -> operatorStack.add(it as Token.Operator)
                Token.Operator.RIGHT_PAREN -> {
                    try {
                        while (true) {
                            val pop = operatorStack.pop()
                            if (pop == Token.Operator.LEFT_PAREN) break
                            output.add(pop)
                        }
                    } catch (_: EmptyStackException) {
                        raiseCompileError("paren does not match") // modify error message later
                    }
                }
                is Token.Operator -> {
                    val operatorRank = it.getRank()
                    val condition: () -> Boolean =
                        when (it.combineDirection) {
                            Token.Operator.CombineDirection.LEFT -> { { operatorStack.last().getRank() <= operatorRank } }
                            Token.Operator.CombineDirection.RIGHT -> { { operatorStack.last().getRank() < operatorRank } }
                        }

                    while (operatorStack.isNotEmpty()) {
                        if (condition()) output.add(operatorStack.pop())
                        else break
                    }
                    operatorStack.push(it)
                }
                else -> println(it)
            }
        }
        while (!operatorStack.isEmpty()) {
            when (val pop = operatorStack.pop()) {
                Token.Operator.LEFT_PAREN, Token.Operator.RIGHT_PAREN -> raiseCompileError("paren does not match") // modify error message later
                else -> output.add(pop)
            }
        }
        return output
    }
}
