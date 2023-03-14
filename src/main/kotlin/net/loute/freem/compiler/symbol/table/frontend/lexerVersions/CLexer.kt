package net.loute.freem.compiler.symbol.table.frontend.lexerVersions

import net.loute.freem.compiler.symbol.table.frontend.token.TToken
import net.loute.freem.compiler.symbol.table.frontend.token.TokenType
import net.loute.freem.compiler.symbol.table.frontend.token.TokenTypes
import net.loute.freem.compiler.util.range.mutableStringRangeOf

object CLexer {
    fun lexicalAnalyse(sourceCode: String, pathname: String? = null): List<TToken> = lexicalAnalyse(sourceCode.iterator(), pathname)
    fun lexicalAnalyse(iterator: CharIterator, pathname: String? = null): List<TToken> {
        val tokenList: MutableList<TToken> = mutableListOf()

        fun push(type: TokenType.Abstract) = tokenList.add(TToken.Abstract(type))

        while (iterator.hasNext()) {
            val lexemeBuilder: StringBuilder = StringBuilder()
            val currentRange = mutableStringRangeOf()

            fun push(type: TokenType.Static) = tokenList.add(TToken.Static(type, currentRange))
            fun push(type: TokenType.Dynamic) = tokenList.add(TToken.Dynamic(type, lexemeBuilder.toString(), currentRange))
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