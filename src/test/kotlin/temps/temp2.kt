package net.loute.freem.compiler.temps

import net.loute.freem.compiler.symbolTable.frontend.Lexer
import net.loute.freem.compiler.symbolTable.frontend.Parser
import net.loute.freem.compiler.util.pipe
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    print("enter an expression > ")
    val expression = scanner.nextLine()
    val tokenArray = expression pipe Lexer::lexicalAnalyse
    print("${tokenArray.joinToString(" ") { it.toString() }} = ")
    tokenArray pipe Parser::parseAnalyse
}