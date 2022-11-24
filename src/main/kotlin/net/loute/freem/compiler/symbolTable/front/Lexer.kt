package net.loute.freem.compiler.symbolTable.front

import net.loute.freem.compiler.FreemCompiler
import net.loute.freem.compiler.symbolTable.front.token.Token

object Lexer {
    class TokenArray: ArrayList<Token>()
    fun lexicalAnalyse(freemCode: FreemCompiler.FreemCode) = TokenArray().apply {
        object {
            val sourceCode = freemCode.content
            var start = 0
            var current = 0
            var line = 1

            val isEnd get() = current >= sourceCode.length
            val peek get() = sourceCode[current]
            fun advance(offset: Int = 1) = sourceCode[current].run { current += offset; this }

            fun toStringArray(charArray: CharArray) = charArray.map { it.toString() }.toTypedArray()
            fun find(vararg target: String) = if (!isEnd) target.find { sourceCode.substring(current).startsWith(it) } else null
            fun match(vararg target: String) = find(*target) != null
            fun matchAdvance(vararg target: String) = if (match(*target)) { advance(find(*target)!!.length);true } else false
            fun find(vararg target: Char) = find(*toStringArray(target))
            fun match(vararg target: Char) = match(*toStringArray(target))
            fun matchAdvance(vararg target: Char) = matchAdvance(*toStringArray(target))

            fun whileAdvance(condition: () -> Boolean) { while (!isEnd && condition()) advance() }
            val stringAt get() = sourceCode.substring(start until current)
            fun addToken(type: Token.TokenType) { add(Token(type, stringAt)) }

            inline fun Boolean.then(block: () -> Unit) = run { if (this) block(); this }
            inline fun <T> T?.be(block: T.() -> Unit) = (this != null).then { this!!.block() }

            fun scan() {
                val regex = Regex(
                    "^(\\s+|[a-zA-Z_][0-9a-zA-Z_]*|'\\?.'|\"\"\"(.|\\n)*\"\"\"|\".*\"|[0-9]+(\\.[0-9]+(u|U)?(f|F)?|(u|U)?(b|B|s|S|l|L|f|F)?))"
                )
                while (!isEnd) {
                    start = current
                    regex.find(sourceCode.substring(start)).be {
                        advance(value.length)
                        line += value.count { it == '\n' }
                        if (value.isNotBlank()) addToken(Token.Type.Keyword.table[value]?:Token.Type.Operator.table[value]?:Token.Type.IDENTIFIER)
                    }
                    /*
                    when {
                        matchAdvance('\n') -> line++
                        peek.isWhitespace() -> whileAdvance { peek.isWhitespace() }
                        peek.toString().matches(Regex("[a-zA-Z_]")) -> {
                            whileAdvance { peek.toString().matches(Regex("[a-zA-Z0-9_]")) }
                            addToken(Token.Type.Keyword.table[stringAt]?: Token.Type.IDENTIFIER)
                        }
                        else -> {
                            mapOf(
                                *Token.Type.Operator.table.toList().toTypedArray(),
                                "\n" to { line++ },
                                "//" to { whileAdvance { !match('\n') } },
                                "/*" to { whileAdvance { if (match('\n')) line++;!matchAdvance("*/") } },
                                "'" to {
                                    advance();matchAdvance('\\');advance()
                                    if (matchAdvance('\'')) addToken(Token.Type.LITERAL)
                                    else raiseCompileError("Incorrect character literal(line:$line)")
                                },
                                "\"" to {
                                    advance()
                                    if (matchAdvance('"') && matchAdvance('"')) {
                                        /*while (!match('"') && !isEnd) {
                                            matchAdvance('\\')
                                            advance()
                                        }*/
                                        // late
                                    } else {
                                        while (!match('"')) {
                                            if (match('\n') || isEnd) raiseCompileError("Incorrect string literal(line:$line)")
                                            matchAdvance('\\')
                                            advance()
                                        }
                                        if (matchAdvance('"')) addToken(Token.Type.LITERAL)
                                        else raiseCompileError("Incorrect string literal(line:$line)")
                                    }
                                }
                            ).filter { sourceCode.substring(start until min(start + it.key.length, sourceCode.length)) == it.key }.run {
                                if (!isEmpty()) {
                                    toList().sortedWith(compareBy { it.first.length }).last().run {
                                        second.run {
                                            when (this) {
                                                is Token.Type.Operator -> {
                                                    current = start + first.length
                                                    addToken(this)
                                                }
                                                is Function0<*> -> this()
                                                else -> {}
                                            }
                                        }
                                    }
                                } else raiseCompileError("Unexpected token: $peek(line:$line)")
                            }
                        }
                    }
                    */
                }
            }
        }.scan()
    }
}