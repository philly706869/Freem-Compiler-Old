package net.loute.freem.compiler.symbolTable.front

import net.loute.freem.compiler.FreemCompiler
import net.loute.freem.compiler.symbolTable.front.token.Token
import net.loute.freem.compiler.symbolTable.raiseCompileError
import net.loute.freem.compiler.util.safe

object Lexer {
    class TokenArray: ArrayList<Token>()

    private val skipRegex = "^(\\s+|\\/\\/.*|\\/\\*(.|\\n)*\\*\\/)".toRegex()
    private val normalRegex = "^([a-zA-Z_][0-9a-zA-Z_]*|${
        run {
            Token.Type.Operator.table.map {
                it.key.map { string -> "\\" + string }.joinToString("")
            }.sortedWith(compareBy { it.length }).reversed().joinToString("|")
        }
    })".toRegex()
    private val literalRegex =
        "^(\\/(\\\\.|[^\\/\\\\])*\\/g?m?i?|'(\\\\.|[^\\\\])'|\"\"\"(\\\\[\\s\\S]|[^\"\\\\])*\"\"\"|\"(\\\\.|[^\"\\\\])*\"|\\d+(u|U)?(b|B|s|S|l|L|f|F)?|\\d*\\.\\d+(u|U)?(f|F)?)"
            .toRegex()

    fun lexicalAnalyse(freemCode: FreemCompiler.FreemCode) = TokenArray().apply {
        val sourceCode = freemCode.content
        var start = 0
        var current = 0
        var line = 1

        fun stringSince() = sourceCode.substring(start)
        fun advance(offset: Int = 1) = sourceCode[current].run { current += offset; this }
        fun addToken(type: Token.TokenType, lexeme: String = sourceCode.substring(start until current)) { add(Token(type, lexeme)) }

        val process = arrayOf<() -> Boolean>(
            {
                skipRegex.find(stringSince()).safe {
                    with(value.count { (it == '\n') }) { if (this > 0) addToken(Token.Type.LINEBREAK, "\n"); line += this }
                    advance(value.length)
                }
            },
            {
                normalRegex.find(stringSince()).safe {
                    advance(value.length); addToken(Token.Type.Keyword.table[value]?:Token.Type.Operator.table[value]?:Token.Type.IDENTIFIER)
                }
            },
            { literalRegex.find(stringSince()).safe { advance(value.length); addToken(Token.Type.LITERAL) } }
        )
        while (current < sourceCode.length) {
            start = current
            if (!process.any { it() }) {
                val column = ".+\$".toRegex().find(sourceCode.substring(0..current))!!.value.length
                raiseCompileError("Unexpected token ${sourceCode[current]}(${line}:${column})")
            }
        }
    }
}