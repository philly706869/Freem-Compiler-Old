package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.symbol.table.frontend.token.*

class Lexer(val sourceCode: String, val pathname: String? = null/*, val tokenTypeSet: TokenTypeSet*/) {
//    constructor(sourceCode: String, pathname: String? = null, tokenTypeSetBuilderBlock: TokenTypeSetBuilderBlock)
//    : this(sourceCode, pathname, TokenTypeSetBuilder(tokenTypeSetBuilderBlock))

    fun lexicalAnalyse(): List<Token> {
        val tokenList = mutableListOf<Token>()
        var index = 0
        var column = 0
        var line = 0
        val iterator = sourceCode.iterator()

        while (iterator.hasNext()) {
            when (iterator.nextChar()) {

                else -> when {

                }
            }
        }
        tokenList.add(Token(TokenTypes.EOF))

        return tokenList.toList()
    }
}