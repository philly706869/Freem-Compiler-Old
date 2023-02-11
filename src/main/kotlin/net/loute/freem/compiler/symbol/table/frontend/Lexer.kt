package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.symbol.table.frontend.token.*

class Lexer(val sourceCode: String, val pathname: String? = null/*, val tokenTypeSet: TokenTypeSet*/) {
//    constructor(sourceCode: String, pathname: String? = null, tokenTypeSetBuilderBlock: TokenTypeSetBuilderBlock)
//    : this(sourceCode, pathname, TokenTypeSetBuilder(tokenTypeSetBuilderBlock))

    fun lexicalAnalyse(): List<Token> {
        //TODO("Not yet implemented")
        val tokenList = mutableListOf<Token>()
        var startIndex = 0
        var column = 0
        var line = 0
        var currentIndex = 0

        val length = sourceCode.length
        fun isEnd() =  currentIndex >= length

        fun advance(): Char {
            val result = sourceCode[currentIndex]
            currentIndex += 1
            column += 1
            return result
        }
        fun peek() = sourceCode.getOrNull(currentIndex)

        while (!isEnd()) {
            startIndex = currentIndex
            val char = advance()

        }
        tokenList.add(Token(TokenTypes.EOF))

        return tokenList.toList()
    }
}

/*
when (char) {
                ';' -> {

                }
                '\n' -> {

                }

                '=' -> {
                    when (peek()) {
                        '>' -> {}
                        '=' -> {
                            '=' -> {}
                            else -> {}
                        }
                        else -> {

                        }
                    }
                }
                '+' -> {
                    '+' -> {}
                    '=' -> {}
                    else -> {}
                }
                '-' -> {
                    '>' -> {}
                    '-' -> {}
                    '=' -> {}
                    else -> {}
                }
                '*' -> {
                    '*' -> {}
                    '=' -> {}
                    else -> {}
                }
                '/' -> {
                    '=' -> {}
                    else -> {}
                }
                '%' -> {
                    '=' -> {}
                    else -> {}
                }
                '!' -> {
                    '!' -> {}
                    '=' -> {
                        '=' -> {}
                        else -> {}
                    }
                    else -> {}
                }

                '<' -> {
                    '<' -> {}
                    '=' -> {}
                    else -> {}
                }
                '>' -> {
                    '>' -> {}
                    '=' -> {}
                    else -> {}
                }

                '&' -> {
                    '&' -> {}
                    else -> {}
                }
                '|' -> {
                    '|' -> {}
                    else -> {}
                }

                '(' -> {}
                ')' -> {}
                '{' -> {}
                '}' -> {}
                '[' -> {}
                ']' -> {}

                '.' -> {}
                ',' -> {}
                '?' -> {}
                ':' -> {
                    ':' -> {}
                    else -> {}
                }

                '@' -> {}

                '^' -> {}
                '~' -> {}
                else -> when {
                    char.isWhitespace() -> {

                    }
                    char.isLowerCase() || char.isUpperCase() || char == '_' -> {

                    }
                    char.isDigit() -> {

                    }
                }
            }
*/
