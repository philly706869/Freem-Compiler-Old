package net.loute.freem.compiler.symbolTable.frontend

import net.loute.freem.compiler.symbolTable.CompileException
import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.util.*
import java.io.File
import java.nio.charset.Charset

private typealias MatchResultTransformer<R> = MatchResult.() -> R

private fun <R: Token> findMatches(string: String, vararg chains: Pair<Regex, MatchResultTransformer<R>>) =
    chains
        .asSequence()
        .firstNotNullOf { (regex, block) -> regex.find(string)?.run(block) }

object Lexer {
    fun lexicalAnalyse(file: File, charset: Charset = Charsets.UTF_8): Array<Token> {
        var code = file.readText(charset)
        val tokenArray = ArrayList<Token>()
        var index = 0
        var line = 1

        fun codeSince() = code.substring(index)

        /**
         * The goal of this function is match regex from (return value of) `codeSince` then
         * add its length to index
         * then run callback
         *
         * @return whether the regex has matched
         */
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
                Token.Operator.table.toList()
                    .sortedWith(compareBy { it.first.length })
                    .reversed()
                    // 이름 지으세요
                    //
                    .any {
                        codeSince().startsWith(it.first).then {
                            index += it.first.length
                            tokenArray.add(it.second)
                        }
                    } // 안이
                // 건물 밀고 다시 짓는중인데 그대로 쓸수있는게 오히려
            },
        )

        while (index < code.length) {
            if (!runProcessor(processor)) {
                val column = index - code.lastIndexOf('\n').coerceAtLeast(0) + 1 // expression before `+ 1` is still an index
                throw CompileException("Unexpected token ${code[index]}(${line}:${column})", "", line, column, index)
            }
        }
        return tokenArray.toTypedArray()
    }
}
