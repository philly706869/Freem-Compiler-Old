package temps

import net.loute.freem.compiler.symbolTable.frontend.Lexer
import java.io.File

fun main() {
    val code = File("src/test/resources/sample-code.fr").readText()
    val result = Lexer.lexicalAnalyse(code)
    println(result.joinToString("\n"))
}
