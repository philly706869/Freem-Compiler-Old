package net.loute.freem.compiler.temps

import net.loute.freem.compiler.symbol.table.frontend.Lexer
import net.loute.freem.compiler.symbol.table.frontend.Parser
import net.loute.freem.compiler.util.pipe

fun main() {
    val tokenArray = Lexer.lexicalAnalyse("")
    print("${tokenArray.joinToString(" ") { it.toString() }} = ")
    tokenArray pipe Parser::parseAnalyse
}