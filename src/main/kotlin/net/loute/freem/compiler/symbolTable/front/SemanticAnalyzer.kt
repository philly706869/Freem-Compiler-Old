package net.loute.freem.compiler.symbolTable.front

import net.loute.freem.compiler.symbolTable.front.token.Token

object SemanticAnalyzer {
    class AbstractSyntaxTree: ArrayList<Token>()
    operator fun invoke(syntaxTree: Parser.SyntaxTree) = AbstractSyntaxTree().apply {

    }
}