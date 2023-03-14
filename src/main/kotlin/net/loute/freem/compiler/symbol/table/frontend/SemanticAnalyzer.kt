package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.symbol.table.frontend.token.TToken

object SemanticAnalyzer {
    class AbstractSyntaxTree: ArrayList<TToken>()
    fun semanticAnalyse(syntaxTree: Parser.SyntaxTree) = AbstractSyntaxTree().apply {

    }
}