package net.loute.freem.compiler.symbolTable.front

import net.loute.freem.compiler.FreemCompiler
import net.loute.freem.compiler.symbolTable.front.token.Token
import net.loute.freem.compiler.symbolTable.raiseCompileError
import net.loute.freem.compiler.util.safe

object Lexer {
    private val skipRegex = "^(\\s+|//.*|/\\*[\\s\\S]*\\*/)+".toRegex()
    private val stringRegex = "^[A-Za-z_]\\w*".toRegex()
    private val operatorRegex = "^(${
        run {
            Token.Operator.table.map {
                it.key.map { string -> "\\" + string }.joinToString("")
            }.sortedWith(compareBy { it.length }).reversed().joinToString("|")
        }
    })".toRegex()
    private val literalRegex =
        "^((\\d*\\.\\d+|\\d+\\.\\d*)[uU]?[fF]?|\\d+[uU]?[bBsSlLfF]?|'(\\\\.|[^\\\\])'|\"\"\"(\\\\[\\s\\S]|[^\"\\\\])*\"\"\"|\"(\\\\.|[^\"\\\\])*\"|/(\\\\.|[^/\\\\])+/[igmuys]*)"
            .toRegex()

    fun lexicalAnalyse(code: String): FreemCompiler.TokenArray {
        val tokenArray = FreemCompiler.TokenArray()
        var start = 0
        var current = 0
        var line = 1

        fun stringAt() = code.substring(start until current)

        infix fun Regex.process(block: MatchResult.() -> Unit) =
            find(code.substring(start)).safe {
                current += value.length
                block()
            }
        val processorInterface = arrayOf<() -> Boolean>(
            {
                skipRegex process {
                    with(value.count { (it == '\n') }) {
                        if (this > 0) tokenArray.add(Token.LINEBREAK)
                        line += this
                    }
                }
            },
            { stringRegex process { tokenArray.add(Token.Keyword.table[value]?:Token.IDENTIFIER(stringAt())) } },
            { operatorRegex process { tokenArray.add(Token.Operator.table[value]!!) } },
            { literalRegex process { tokenArray.add(Token.LITERAL(stringAt())) } }
        )

        while (current < code.length) {
            start = current
            if (!processorInterface.any { it() }) {
                val column = ".+\$".toRegex().find(code.substring(0..current))!!.value.length
                raiseCompileError("Unexpected token ${code[current]}(${line}:${column})")
            }
        }

        return tokenArray
    }
}