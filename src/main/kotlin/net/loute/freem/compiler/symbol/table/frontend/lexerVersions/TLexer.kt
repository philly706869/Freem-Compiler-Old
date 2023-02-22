package net.loute.freem.compiler.symbol.table.frontend.lexerVersions

import kotlinx.coroutines.*
import net.loute.freem.compiler.CompileException
import net.loute.freem.compiler.symbol.table.frontend.token.*
import net.loute.freem.compiler.util.collection.Trie
import net.loute.freem.compiler.util.collection.TrieNode
import net.loute.freem.compiler.util.collection.toTrie
import net.loute.freem.compiler.util.isAlpha
import net.loute.freem.compiler.util.range.mutableStringRangeOf

object FreemLexer: Lexer(
    { // identifier
        matchNext(Char::isAlpha)

    },
    static = static(
        *TokenTypes.Operator.values(),
        *TokenTypes.Keyword.values(),
        *TokenTypes.Separator.values()
    ),
    eof = TokenTypes.EOF
)

fun static(vararg staticTokenType: TokenType.Static) = Statics(staticTokenType.asList())
data class Statics(val staticTokenTypes: List<TokenType.Static>)

private typealias ScannerBlock = suspend Lexer.Scanner.() -> Unit

open class Lexer(vararg scanner: ScannerBlock, static: Statics? = null, private val eof: TokenType.Abstract? = null) {
    private val process: List<ScannerBlock>

    init {
        val process: MutableList<ScannerBlock> = scanner.toMutableList()

        if (static != null) {
            val staticTrie: Trie = static.staticTokenTypes.map { it.staticValue }.toTrie()
            val staticMap: Map<String, TokenType.Static> = static.staticTokenTypes.associateBy { it.staticValue }
            val staticProcessBlock: ScannerBlock = {
                var currentTrie: TrieNode = staticTrie
                while (hasNext()) {
                    val next = next()
                    currentTrie = currentTrie[next]?:exit()
                }
                commit(staticMap[lexeme]?:exit())
            }
            process.add(0, staticProcessBlock)
        }

        this.process = process
    }

    interface Scanner {
        val lexeme: String
        fun commit(tokenType: TokenType)
        fun commitAll(tokenTypes: Collection<TokenType.Abstract>)
        fun push(): Nothing
        fun exit(): Nothing
        fun error(message: String): Nothing
        fun matchNext(char: Char)
        fun matchNext(condition: (Char) -> Boolean)
        fun hasNext(): Boolean
        suspend fun next(): Char
    }

    private object Exit: Throwable()

    fun lexicalAnalyse(sourceCode: String, pathname: String? = null): List<Token> = lexicalAnalyse(sourceCode.iterator(), pathname)
    fun lexicalAnalyse(iterator: Iterator<Char>, pathname: String? = null): List<Token> {
        val tokenList = mutableListOf<Token>()
        val currentRange = mutableStringRangeOf()

        runBlocking {
            while (iterator.hasNext()) {
                val next = iterator.next()
                val jobs = process.map { block ->
                    launch {
                        try {
                            object: Scanner {
                                override val lexeme: String
                                    get() = TODO("Not yet implemented")

                                override fun commit(tokenType: TokenType) {
                                    TODO("Not yet implemented")
                                }

                                override fun commitAll(tokenTypes: Collection<TokenType.Abstract>) {
                                    TODO("Not yet implemented")
                                }

                                override fun push(): Nothing {
                                    TODO("Not yet implemented")
                                }

                                override fun error(message: String): Nothing = throw CompileException(message)
                                override fun matchNext(char: Char) {
                                    TODO("Not yet implemented")
                                }

                                override fun matchNext(condition: (Char) -> Boolean) {
                                    TODO("Not yet implemented")
                                }

                                override fun exit(): Nothing = throw Exit
                                override fun hasNext(): Boolean = iterator.hasNext()
                                override suspend fun next(): Char {
                                    yield()
                                    return TODO("Not yet implemented")
                                }
                            }.block()
                        } catch (_: Exit) {}
                    }
                }
                joinAll(*jobs.toTypedArray())
            }
        }

        if (eof != null) tokenList.add(Token(eof))

        return tokenList
    }
}
