package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.symbol.table.frontend.token.Token

object SemanticAnalyzer {
    class AbstractSyntaxTree: ArrayList<Token>()
    fun semanticAnalyse(syntaxTree: Parser.SyntaxTree) = AbstractSyntaxTree().apply {

    }
}