package net.loute.freem.compiler.symbolTable.frontend

import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.symbolTable.throwCompileError
import net.loute.freem.compiler.util.*

/*
interface LexerStruct {
    val SKIP: Array<Regex>
    val KEYWORD: Array<Pair<String, *>>
    val IDENTIFIER: Array<Pair<Regex, *>>
    val OPERATOR: Int
    val LITERAL: Int
}
*/

private typealias MatchResultTransformer<R> = MatchResult.() -> R

private fun <R: Token> findMatches(string: String, vararg chains: Pair<Regex, MatchResultTransformer<R>>) =
    chains
        .asSequence()
        .firstNotNullOf { (regex, block) -> regex.find(string)?.run(block) }

object Lexer {
    fun lexicalAnalyse(code: String): Array<Token> {
        val tokenArray = ArrayList<Token>()
        var index = 0
        var line = 1

        fun codeSince() = code.substring(index)

        infix fun Regex.findThen(block: MatchResult.() -> Unit) =
            find(codeSince()).safe {
                index += value.length
                block()
            } != null

        val token = findMatches(
            codeSince(),
            "^(\\s+|//.*|/\\*[\\s\\S]*\\*/)+".toRegex() to {
                Token.LINEBREAK//??
            },
            "^[A-Za-z_]\\w*".toRegex() to {
                Token.Keyword.table[value] ?: Token.IDENTIFIER(value)
            },
            "^(\\d*\\.\\d+|\\d+\\.\\d*)".toRegex() to {
                TODO()
            },
            "^\\d+(_\\d+)*".toRegex(RegexOption.IGNORE_CASE) to {
                arrayOf(
                    Token.Literal.Number.UBYTE,
                    Token.Literal.Number.USHORT,
                    Token.Literal.Number.ULONG,
                    Token.Literal.Number.UINT,
                    Token.Literal.Number.BYTE,
                    Token.Literal.Number.SHORT,
                    Token.Literal.Number.LONG,
                    Token.Literal.Number.FLOAT,
                    Token.Literal.Number.INT,
                )
                    .find { it.suffixRegex?.matches(value) != false }!!
                    .createInstance(value)
            },
            "^'(\\\\.|[^\\\\])'".toRegex() to {
                Token.Literal.Text.CHAR(value)
            },
            "^(\"\"\"(\\\\[\\s\\S]|[^\"\\\\])*\"\"\"|\"(\\\\.|[^\"\\\\])*\")".toRegex() to {
                Token.Literal.Text.STRING(value)
            },
            "^/(\\\\.|[^/\\\\])+/[igmuys]*".toRegex() to {
                Token.Literal.Text.REGEX(value)
            }
        )
        // runs callback sequentially under condition `if there's no item that has matched previously`
        val processor = processorOf(
            // white space, comment
            { "^(\\s+|//.*|/\\*[\\s\\S]*\\*/)+"         .toRegex() findThen { line += value.count { (it == '\n') }.then { tokenArray.add(Token.LINEBREAK) } } },

            // keyword, identifier
            { "^[A-Za-z_]\\w*"                          .toRegex() findThen { tokenArray.add(Token.Keyword.table[value]?: Token.IDENTIFIER(value)) } },

            // number
            {
                "^(\\d*\\.\\d+|\\d+\\.\\d*)".toRegex() findThen {
                    val lexeme = value
                    /*
                    runProcess(
                        Token.Literal.Number.FLOAT,
                        { tokenArray.add(Token.Literal.Number.DOUBLE(value)) },
                    )
                    */
                }
            },
            {
                "^\\d+(_\\d+)*".toRegex(RegexOption.IGNORE_CASE) findThen {
                    val lexeme = value
                    arrayOf(
                        Token.Literal.Number.UBYTE,
                        Token.Literal.Number.USHORT,
                        Token.Literal.Number.ULONG,
                        Token.Literal.Number.UINT,
                        Token.Literal.Number.BYTE,
                        Token.Literal.Number.SHORT,
                        Token.Literal.Number.LONG,
                        Token.Literal.Number.FLOAT,
                        Token.Literal.Number.INT,
                    )
                        .find { it.suffixRegex?.matches(value) != false }!!
                        .createInstance(lexeme)
                        .pipe(tokenArray::add)
                }
            },

            // text
            { "^'(\\\\.|[^\\\\])'"                                              .toRegex() findThen { tokenArray.add(Token.Literal.Text.CHAR(value)) } },
            { "^(\"\"\"(\\\\[\\s\\S]|[^\"\\\\])*\"\"\"|\"(\\\\.|[^\"\\\\])*\")" .toRegex() findThen { tokenArray.add(Token.Literal.Text.STRING(value)) } },
            { "^/(\\\\.|[^/\\\\])+/[igmuys]*"                                   .toRegex() findThen { tokenArray.add(Token.Literal.Text.REGEX(value)) } },

            // operator
            {
                Token.Operator.table.toList().sortedWith(compareBy { it.first.length }).reversed().any {
                    codeSince().startsWith(it.first).then {
                        index += it.first.length
                        tokenArray.add(it.second)
                    }
                }
            },
        )

        while (index < code.length) {
            if (!runProcessor(processor)) {
                val column = index - code.lastIndexOf('\n').coerceAtLeast(0) + 1 // expression before `+ 1` is still an index
                throwCompileError("Unexpected token ${code[index]}(${line}:${column})", "", line, column, index)
            }
        }
        return tokenArray.toTypedArray()
    }
}