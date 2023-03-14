package net.loute.freem.compiler.symbol.table.frontend.lexerVersions

/*
fun lexicalAnalyze(sourceCode: String, pathname: String? = null): List<Token> {
    val tokenList = mutableListOf<Token>()
    val currentLocation = mutableStringLocationOf()
    val currentRange = mutableStringRangeOf(mutableStringLocationOf(), currentLocation)
    val lexemeBuilder = StringBuilder()

    fun isEnd() = sourceCode.length > currentLocation.index
    fun advance(): Char {
        val char = sourceCode[currentLocation.index]
        currentLocation.index++
        currentLocation.column++
        lexemeBuilder.append(char)
        return char
    }
    fun peek() = sourceCode[currentLocation.index]

    while (!isEnd()) {
        lexemeBuilder.clear()
        currentRange.start.update(currentLocation)

        var char = advance()
        val tokenType: TokenType.InlineTokenType = when (char) {
            ';' -> TokenTypes.Separator.SEMICOLON
            '\n' -> {
                currentLocation.column = 1
                currentLocation.row++
                TokenTypes.Separator.LINEBREAK
            }
            '=' -> {
                when (peek()) {
                    '=' -> {
                        when (peek()) {
                            '=' -> {

                            }
                            else -> {

                            }
                        }

                    }
                    else -> {

                    }
                }
            }
            '+' -> {
                '+' -> {

                }
                '=' -> {

                }
                '>' -> {

                }
                else -> {

                }
            }
            '-' -> {
                '-' -> {

                }
                '=' -> {

                }
                '>' -> {

                }
                else -> {

                }
            }
            '*' -> {
                '*' -> {

                }
                '=' -> {

                }
                else -> {

                }
            }
            '/' -> {
                '/' -> {

                }
                '*' -> {

                }
                '=' -> {

                }
                else -> {

                }
            }
            '%' -> {
                '=' -> {

                }
                else -> {

                }
            }
            '!' -> {
                '!' -> {

                }
                '=' -> {
                    '=' -> {

                }
                    else -> {

                }
                }
                else -> {

                }
            }
            '<' -> {
                '<' -> {

                }
                '=' -> {

                }
                else -> {

                }
            }
            '>' -> {
                '>' -> {

                }
                '=' -> {

                }
                else -> {

                }
            }
            '&' -> {
                '&' -> {

                }
                else -> {

                }
            }
            '|' -> {
                '|' -> {

                }
                else -> {

                }
            }
            '(' -> {

            }
            ')' -> {

            }
            '{' -> {

            }
            '}' -> {

            }
            '[' -> {

            }
            ']' -> {

            }
            '.' -> {

            }
            ',' -> {

            }
            '?' -> {

            }
            ':' -> {
                ':' -> {

                }
                else -> {

                }
            }
            '\"' -> {

            }
            '@' -> {

            }
            '^' -> {

            }
            '~' -> {

            }
            else -> {

            }
        }
        when (tokenType) {
            is TokenType.Static -> tokenList.add(Token(tokenType, currentRange))
            is TokenType.Dynamic -> tokenList.add(Token(tokenType, lexemeBuilder.toString(), currentRange))
        }
    }

    tokenList.add(Token(TokenTypes.EOF))

    return tokenList
}*/
