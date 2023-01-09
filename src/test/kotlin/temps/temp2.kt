package net.loute.freem.compiler.temps

import net.loute.freem.compiler.symbolTable.frontend.Lexer
import net.loute.freem.compiler.symbolTable.frontend.Parser
import net.loute.freem.compiler.util.pipe
import java.util.Scanner

fun main() {
    val tokenArray = java.io.File("") pipe Lexer::lexicalAnalyse
    print("${tokenArray.joinToString(" ") { it.toString() }} = ")
    tokenArray pipe Parser::parseAnalyse
}