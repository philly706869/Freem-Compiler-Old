import net.loute.freem.compiler.symbol.table.frontend.Lexer
import java.io.File

fun main() {
    val result = Lexer.lexicalAnalyse(File("src/test/resources/sample-code.fr").readText())
    println(result.joinToString("\n"))
}
