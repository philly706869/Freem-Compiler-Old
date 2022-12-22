package net.loute.freem.compiler.symbolTable.frontend

import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.symbolTable.throwCompileError
import net.loute.freem.compiler.util.process
import net.loute.freem.compiler.util.processorOf
import net.loute.freem.compiler.util.safe
import net.loute.freem.compiler.util.then

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
            }
        val processorInterface = processorOf(
            // white space, comment
            { "^(\\s+|//.*|/\\*[\\s\\S]*\\*/)+"         .toRegex() findThen { line += value.count { (it == '\n') }.then { tokenArray.add(Token.LINEBREAK) } } },

            // keyword, identifier
            { "^[A-Za-z_]\\w*"                          .toRegex() findThen { tokenArray.add(Token.Keyword.table[value]?:Token.IDENTIFIER(value)) } },

            // number
            {
                "^(\\d*\\.\\d+|\\d+\\.\\d*)".toRegex() findThen {
                    val number = value
                    process(
                        { "^[uU][fF]"   .toRegex() findThen { tokenArray.add(Token.Literal.Number.UFLOAT(number)) } },
                        { "^[uU]"       .toRegex() findThen { tokenArray.add(Token.Literal.Number.UDOUBLE(number)) } },
                        { "^[fF]"       .toRegex() findThen { tokenArray.add(Token.Literal.Number.FLOAT(number)) } },
                        { tokenArray.add(Token.Literal.Number.DOUBLE(number)) },
                    )
                }
            },
            {
                "^\\d+[uU]?[bBsSlLfF]?".toRegex() findThen {
                    val number = value
                    process(
                        { "^[uU][bB]"   .toRegex() findThen { tokenArray.add(Token.Literal.Number.UBYTE(number)) } },
                        { "^[uU][sS]"   .toRegex() findThen { tokenArray.add(Token.Literal.Number.USHORT(number)) } },
                        { "^[uU][lL]"   .toRegex() findThen { tokenArray.add(Token.Literal.Number.ULONG(number)) } },
                        { "^[uU][fF]"   .toRegex() findThen { tokenArray.add(Token.Literal.Number.UFLOAT(number)) } },
                        { "^[uU]"       .toRegex() findThen { tokenArray.add(Token.Literal.Number.UINT(number)) } },
                        { "^[bB]"       .toRegex() findThen { tokenArray.add(Token.Literal.Number.BYTE(number)) } },
                        { "^[sS]"       .toRegex() findThen { tokenArray.add(Token.Literal.Number.SHORT(number)) } },
                        { "^[lL]"       .toRegex() findThen { tokenArray.add(Token.Literal.Number.LONG(number)) } },
                        { "^[fF]"       .toRegex() findThen { tokenArray.add(Token.Literal.Number.FLOAT(number)) } },
                        { tokenArray.add(Token.Literal.Number.INT(number)) },
                    )
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
            if (!process(*processorInterface))
                throwCompileError {
                    val column = ".+\$".toRegex().find(code.substring(0..index))!!.value.length
                    "Unexpected token ${code[index]}(${line}:${column})"
                }
        }
        return tokenArray.toTypedArray()
    }
}