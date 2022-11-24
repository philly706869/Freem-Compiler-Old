package net.loute.freem.compiler.symbolTable.front

import net.loute.freem.compiler.symbolTable.front.token.Token

object SemanticAnalyzer {
    class AbstractSyntaxTree: ArrayList<Token>()
    fun semanticAnalyse(syntaxTree: Parser.SyntaxTree) = AbstractSyntaxTree().apply {

    }
}