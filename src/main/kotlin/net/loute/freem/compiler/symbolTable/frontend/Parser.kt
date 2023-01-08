package net.loute.freem.compiler.symbolTable.frontend

import net.loute.freem.compiler.symbolTable.frontend.token.MIN_RANK
import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.symbolTable.frontend.token.rank
import net.loute.freem.compiler.symbolTable.throwCompileError
import net.loute.freem.compiler.util.pipe
import net.loute.freem.compiler.util.toDouble
import kotlin.math.pow

//fun String.trimEdge() = substring(1 until length - 1)

object Parser {
    class SyntaxTree
    fun parseAnalyse(tokenArray: Array<Token>) = SyntaxTree().apply {
        object {
            val iterator = tokenArray.iterator()
            var currentToken = iterator.next()

            fun next() = run { currentToken = iterator.next(); currentToken }

            fun express(): Double = term(Token.Operator.MIN_RANK)

            fun term(rank: Int): Double {
                if (rank == Token.Operator.MIN_RANK - 1) return factor()

                var value: Double = term(rank - 1)
                while (iterator.hasNext() && (currentToken as Token.Operator).rank == rank) {
                    val operator = next()
                    if (operator !is Token.Operator || !iterator.hasNext()) throw Exception("invalid express") // temp
                    next()
                    when (operator.combineDirection) {
                        Token.Operator.CombineDirection.LEFT -> value = calculate(operator, value, term(rank - 1))
                        Token.Operator.CombineDirection.RIGHT -> { value = calculate(operator, value, term(rank)); break }
                        else -> {}
                    }
                }
                return value
                /*
                c++

                double n;
                char op;
                if (rank == 7) {//제곱 연산자면
                    n = term(0);
                    if (t.type == _pow) {
                        t = gettoken();//토큰받고
                        n = pow(n, term(7)); //n^term(7)계산
                    }
                }
                //그 외의 연산자
                else {
                    n = term(rank + 1);//우선순위 높은 식 먼저 계산
                    while (oprank(t.type) == rank) { //기호의 우선순위가 rank와 같다면
                        op = t.type;
                        t = gettoken();//토큰받고
                        n=calculate(n, term(rank + 1),op);//계산
                    }
                }
                return n;//결과 반환
                */

                /*
                if (currentToken == Token.Operator.D_STAR) {
                    next()
                    value = calculate(value, term(Token.Operator.MIN_RANK - 1))
                }
                value = term(rank - 1)
                val curToken = currentToken
                if (curToken !is Token.Operator) throw Exception("term error")
                while (curToken.rank == rank) {
                    next()
                    value = calculate(curToken, value, term(rank - 1))
                }
                */
                return value
            }

            fun calculate(operator: Token.Operator, number1: Double, number2: Double): Double = when (operator) {
                Token.Operator.D_STAR -> number1.pow(number2)
                Token.Operator.STAR -> number1 * number2
                Token.Operator.SLASH -> number1 / number2
                Token.Operator.PERCENT -> number1 % number2
                Token.Operator.PLUS -> number1 + number2
                Token.Operator.MINUS -> number1 - number2
                Token.Operator.LESS -> (number1 < number2).toDouble()
                Token.Operator.LESS_EQUAL -> (number1 <= number2).toDouble()
                Token.Operator.GREATER -> (number1 > number2).toDouble()
                Token.Operator.GREATER_EQUAL -> (number1 >= number2).toDouble()
                Token.Operator.NOT_EQ -> (number1 != number2).toDouble()
                Token.Operator.D_EQUAL -> (number1 == number2).toDouble()
                Token.Operator.AND -> (number1 != 0.0 && number2 != 0.0).toDouble()
                Token.Operator.OR -> (number1 != 0.0 || number2 != 0.0).toDouble()
                else -> throw Exception("can't operate") // temp
            }

            fun factor(): Double {
                var value = 0.0
                when (val it = currentToken) {
                    Token.Operator.LEFT_PAREN -> {
                        next()
                        value = express()
                        //if (currentToken != Token.Operator.RIGHT_PAREN) throwCompileError("cannot find character ')'")
                    }
                    Token.Operator.PLUS -> { next(); return factor() }
                    Token.Operator.MINUS -> { next(); return -factor() }
                    Token.Operator.NOT -> { next(); return (factor() == 0.0).toDouble() }
                    is Token.Literal.Number.INT -> value = it.toNumber().toDouble()
                    else -> println(it)
                }
                if (iterator.hasNext()) next()
                return value
            }
        }.express() pipe ::println
    }

    /*
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
    */
}
