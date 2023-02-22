package net.loute.freem.compiler.symbol.table.frontend.lexerVersions

import net.loute.freem.compiler.symbol.table.frontend.token.Token
import net.loute.freem.compiler.symbol.table.frontend.token.TokenType
import net.loute.freem.compiler.symbol.table.frontend.token.TokenTypes
import net.loute.freem.compiler.util.range.mutableStringRangeOf

object CLexer {
    fun lexicalAnalyse(sourceCode: String, pathname: String? = null): List<Token> = lexicalAnalyse(sourceCode.iterator(), pathname)
    fun lexicalAnalyse(iterator: CharIterator, pathname: String? = null): List<Token> {
        val tokenList: MutableList<Token> = mutableListOf()

        fun push(type: TokenType.Abstract) = tokenList.add(Token.Abstract(type))

        while (iterator.hasNext()) {
            val lexemeBuilder: StringBuilder = StringBuilder()
            val currentRange = mutableStringRangeOf()

            fun push(type: TokenType.Static) = tokenList.add(Token.Static(type, currentRange))
            fun push(type: TokenType.Dynamic) = tokenList.add(Token.Dynamic(type, lexemeBuilder.toString(), currentRange))
            /*
            when (char) {
                ';' -> {}
                '\n' -> {}
            }

             */
        }

        push(TokenTypes.EOF)

        return tokenList

//        val lexemeBuilder = StringBuilder()
//
//
//        fun advance() {
//
//        }
//
//
//        while (iterator.hasNext()) {
//            currentRange.apply {
//                start.row = end.row
//                start.column = end.column
//                start.index = end.index
//            }
//            lexemeBuilder.clear()
//            val char = iterator.next()
//            when (char) {
//
//            }
//        }


    }
}