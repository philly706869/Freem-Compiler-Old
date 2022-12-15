package net.loute.freem.compiler.symbolTable.frontend

import net.loute.freem.compiler.symbolTable.frontend.token.Token

object SemanticAnalyzer {
    class AbstractSyntaxTree: ArrayList<Token>()
    fun semanticAnalyse(syntaxTree: Parser.SyntaxTree) = AbstractSyntaxTree().apply {

    }
}