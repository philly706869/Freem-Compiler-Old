package net.loute.freem.compiler.symbolTable.frontend

import net.loute.freem.compiler.FreemCompiler
import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.symbolTable.throwCompileError
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
        var index = 0
        var line = 1

        infix fun Regex.process(block: MatchResult.() -> Unit) =
            find(code.substring(index)).safe {
                index += value.length
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
            { stringRegex process { tokenArray.add(Token.Keyword.table[value]?:Token.IDENTIFIER(value)) } },
            { operatorRegex process { tokenArray.add(Token.Operator.table[value]!!) } },
            { literalRegex process { tokenArray.add(Token.LITERAL(value)) } }
        )

        while (index < code.length) {
            if (!processorInterface.any { it() })
                throwCompileError {
                    val column = ".+\$".toRegex().find(code.substring(0..index))!!.value.length
                    "Unexpected token ${code[index]}(${line}:${column})"
                }
        }
        return tokenArray
    }
}