package net.loute.freem.compiler.symbolTable.frontend

import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.symbolTable.frontend.token.getRank
import net.loute.freem.compiler.symbolTable.throwCompileError
import java.util.*
import kotlin.collections.ArrayList

//fun String.trimEdge() = substring(1 until length - 1)

object Parser {
    class SyntaxTree
    fun parseAnalyse(tokenArray: Array<Token>) = SyntaxTree().apply {
        object {
            val iterator = tokenArray.iterator()
            var currentToken = iterator.next()

            fun express(): Int = less_term()

            fun factor(): Int {
                var value = 0
                when (val it = currentToken) {
                    Token.Operator.LEFT_PAREN -> {
                        currentToken = iterator.next()
                        value = express()
                        if (currentToken != Token.Operator.RIGHT_PAREN) throwCompileError("cannot find character ')'")
                    }
                    is Token.Literal.Number.INT -> value = it.toNumber()
                    else -> println(it)
                }
                if (iterator.hasNext()) currentToken = iterator.next()
                return value
            }

            fun mul_term(): Int {
                var value = factor()
                while (true) {
                    when (currentToken) {
                        Token.Operator.STAR -> { currentToken = iterator.next(); value *= factor() }
                        Token.Operator.SLASH -> { currentToken = iterator.next(); value /= factor() }
                        Token.Operator.PERCENT -> { currentToken = iterator.next(); value %= factor() }
                        else -> break
                    }
                }
                return value
            }

            fun add_term(): Int {
                var value = mul_term()
                while (true) {
                    when (currentToken) {
                        Token.Operator.PLUS -> { currentToken = iterator.next(); value += mul_term() }
                        Token.Operator.MINUS -> { currentToken = iterator.next(); value -= mul_term() }
                        else -> break
                    }
                }
                return value
            }

            fun less_term(): Int {
                var value = add_term()
                while (true) {
                    when (currentToken) {
                        Token.Operator.LESS -> { currentToken = iterator.next(); value = if (value < add_term()) 1 else 0 }
                        Token.Operator.LESS_EQUAL -> { currentToken = iterator.next(); value = if (value <= add_term()) 1 else 0 }
                        Token.Operator.GREATER -> { currentToken = iterator.next(); value = if (value > add_term()) 1 else 0 }
                        Token.Operator.GREATER_EQUAL -> { currentToken = iterator.next(); value = if (value >= add_term()) 1 else 0 }
                        else -> break
                    }
                }
                return value
            }
        }.express().run { println(this) }
    }

    fun shuntingYardWith(tokens: Array<Token>): Array<Token> {
        val output = ArrayList<Token>()
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
                        throwCompileError("paren does not match") // modify error message later
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
                Token.Operator.LEFT_PAREN, Token.Operator.RIGHT_PAREN -> throwCompileError("paren does not match") // modify error message later
                else -> output.add(pop)
            }
        }
        return output.toTypedArray()
    }
}
