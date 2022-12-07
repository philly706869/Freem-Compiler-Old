package temps

import net.loute.freem.compiler.symbolTable.front.Lexer
import net.loute.freem.compiler.symbolTable.front.Parser
import net.loute.freem.compiler.symbolTable.front.token.Token
import java.io.File

fun main() {
    val code = File("src/test/resources/sample-code.fr").readText()
    val tokens = Lexer.lexicalAnalyse(code)
    val result = Parser.shuntingYardWith(tokens)
    println(result.map {
        when (it) {
            is Token.PolymorphicToken -> it.lexeme
            is Token.Operator -> it.value
            else -> it
        }
    }.joinToString(" "))
}

