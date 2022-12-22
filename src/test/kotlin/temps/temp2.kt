package net.loute.freem.compiler.temps

import net.loute.freem.compiler.symbolTable.frontend.Lexer
import net.loute.freem.compiler.symbolTable.frontend.Parser
import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.util.pipe

fun main() {
    (java.io.File("src/test/resources/sample-code.fr").readText() pipe Lexer::lexicalAnalyse)
        .filter { it != Token.LINEBREAK }
        .toTypedArray() pipe
            Parser::parseAnalyse
}
