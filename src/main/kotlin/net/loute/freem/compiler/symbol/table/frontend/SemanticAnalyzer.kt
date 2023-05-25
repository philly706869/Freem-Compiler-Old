package net.loute.freem.compiler.symbol.table.frontend

object SemanticAnalyzer {
    class AbstractSyntaxTree: ArrayList<Token>()
    fun semanticAnalyse(syntaxTree: Parser.SyntaxTree) = AbstractSyntaxTree().apply {

    }
}