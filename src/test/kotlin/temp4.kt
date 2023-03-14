import net.loute.freem.compiler.symbol.table.frontend.FreemLexer

fun main() {
    val result = FreemLexer.lexicalAnalyse("func main val const")
    result.forEach { println(it.type) }
}