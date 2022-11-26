package net.loute.freem.compiler.symbolTable.front

import net.loute.freem.compiler.symbolTable.front.token.Token
import net.loute.freem.compiler.symbolTable.front.token.table.operator.getRank
import net.loute.freem.compiler.symbolTable.raiseCompileError
import java.util.*

//fun String.trimEdge() = substring(1 until length - 1)

object Parser {
    class SyntaxTree: ArrayList<Token>()
    operator fun invoke(tokenArray: Lexer.TokenArray) = SyntaxTree().apply {
        tokenArray.onEach {
            println(
                """
                {
                    type: ${it.type},
                    lexeme: "${it.lexeme}",
                },
                """.trimIndent()
            )
        }

        tokenArray.forEach {
            when (it.type) {
                Token.Type.IDENTIFIER, Token.Type.LITERAL -> {

                }
            }
        }
    }
    private class Statement {
        val output = Lexer.TokenArray()
        val operatorStack = Lexer.TokenArray()
        fun push(token: Token) {
            if (token.type is Token.Type.Operator) {
                if (token.type == Token.Type.Operator.RIGHT_PAREN) popUntilLeftParen()
                else if(token.type != Token.Type.Operator.LEFT_PAREN) pop(token.type.getRank())
                operatorStack.add(token)
            }
        }
        fun pop(num: Int) {
            var topRank = 0
            val isCombineRight = num != 1
            while (!operatorStack.isEmpty()) {
                topRank = operatorStack.last().type.run { if (this is Token.Type.Operator) getRank() else 0 }
                if (isCombineRight) { if (topRank <= num) { output.add(operatorStack.last()); popBack() } }
                else if (topRank < num) { output.add(operatorStack.last()); popBack() } else break
            }
        }
        fun popBack() { operatorStack.run { removeAt(lastIndex) } }
        fun popUntilLeftParen() {
            while (!operatorStack.isEmpty()) {
                if (operatorStack.last().type != Token.Type.Operator.LEFT_PAREN) { output.add(operatorStack.last()); popBack() } else { popBack(); return } }
            raiseCompileError("cannot find '('")
        }

        fun express() {} //식 분석 함수
        fun factor() {} //피연산자, 괄호 등 분석 함수
    }
}