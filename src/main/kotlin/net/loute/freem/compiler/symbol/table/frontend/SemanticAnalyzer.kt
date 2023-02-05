package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.symbol.table.frontend.token.Token_

object SemanticAnalyzer {
    class AbstractSyntaxTree: ArrayList<Token_>()
    fun semanticAnalyse(syntaxTree: Parser.SyntaxTree) = AbstractSyntaxTree().apply {

    }
}