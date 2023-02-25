package net.loute.freem.compiler.symbol.table.frontend.lexerVersions

import kotlinx.coroutines.*
import net.loute.freem.compiler.CompileException
import net.loute.freem.compiler.main.main
import net.loute.freem.compiler.symbol.table.frontend.token.*
import net.loute.freem.compiler.util.collection.Trie
import net.loute.freem.compiler.util.collection.TrieNode
import net.loute.freem.compiler.util.collection.toTrie
import net.loute.freem.compiler.util.isAlpha
import net.loute.freem.compiler.util.pipe
import net.loute.freem.compiler.util.range.mutableStringRangeOf
import java.util.NoSuchElementException

object FreemLexer: Lexer(
    LexerBuilder()
        .addDynamic {
            // identifier
            if (hasNext()) {
                if (next().isAlpha().not()) exit()
                while (hasNext()) {

                }
            } else exit()
        }
        .addDynamic {
            // literal

        }
        .addDynamic {
            //
        }
        .addStatic(TokenTypes.Separator.values())
        .addStatic(TokenTypes.Operator.values())
        .addStatic(TokenTypes.Keyword.values())
        .setEOF(TokenTypes.EOF)
)

private typealias ScannerBlock = suspend Lexer.Scanner.() -> Unit

class LexerBuilder {
    val statics: List<TokenType.Static> get() = innerStatics
    private val innerStatics: MutableList<TokenType.Static> = mutableListOf()
    val dynamics: List<ScannerBlock> get() = innerDynamics
    private val innerDynamics: MutableList<ScannerBlock> = mutableListOf()
    val eof: TokenType.Abstract? get() = innerEof
    private var innerEof: TokenType.Abstract? = null

    fun addStatic(tokenTypes: Array<out TokenType.Static>): LexerBuilder = this.apply { innerStatics.addAll(tokenTypes) }
    fun addStatic(tokenTypes: Sequence<TokenType.Static>): LexerBuilder = this.apply { innerStatics.addAll(tokenTypes) }
    fun addStatic(tokenTypes: Collection<TokenType.Static>): LexerBuilder = this.apply { innerStatics.addAll(tokenTypes) }
    fun addStatic(vararg tokenTypes: TokenType.Static): LexerBuilder = this.apply { innerStatics.addAll(tokenTypes) }
    fun addDynamic(scanner: ScannerBlock): LexerBuilder = this.apply { innerDynamics.add(scanner) }
    fun setEOF(eof: TokenType.Abstract): LexerBuilder = this.apply { innerEof = eof }
}

open class Lexer(lexerBuilder: LexerBuilder) {
    private val process: List<ScannerBlock>
    val eof: TokenType.Abstract? = lexerBuilder.eof

    init {
        val process: MutableList<ScannerBlock> = lexerBuilder.dynamics.toMutableList()

        if (lexerBuilder.statics.isNotEmpty() && lexerBuilder.statics.any { it.staticValue.isEmpty() }.not()) {
            val staticTrie: Trie = lexerBuilder.statics.map { it.staticValue }.toTrie()
            val staticMap: Map<String, TokenType.Static> = lexerBuilder.statics.associateBy { it.staticValue }
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
        fun commitAll(tokenTypes: Array<out TokenType>)
        fun commitAll(tokenTypes: Sequence<TokenType>)
        fun commitAll(tokenTypes: Collection<TokenType>)
        fun commitAll(vararg tokenTypes: TokenType)
        fun exit(): Nothing
        fun error(message: String): Nothing
        fun hasNext(): Boolean
        suspend fun next(): Char
    }

    private object Exit: Throwable()

    fun lexicalAnalyse(sourceCode: String, pathname: String? = null): List<Token> = lexicalAnalyse(sourceCode.iterator(), pathname)
    fun lexicalAnalyse(iterator: CharIterator, pathname: String? = null): List<Token> = runBlocking {
        val tokenList = mutableListOf<Token>()
        val currentRange = mutableStringRangeOf()

        var next: Char? = null
        while (iterator.hasNext()) {
            val processes = process.map { block ->
                object {
                    var exited: Boolean = false
                    var compileException: CompileException? = null
                    val committed: MutableList<Token> = mutableListOf()
                    private val lexemeBuilder = StringBuilder()
                    fun run(): Job = launch {
                        try {
                            object: Scanner {
                                override val lexeme: String get() = lexemeBuilder.toString()

                                override fun commit(tokenType: TokenType) {
                                    when (tokenType) {
                                        is TokenType.Abstract -> Token(tokenType)
                                        is TokenType.Static -> Token(tokenType, mutableStringRangeOf())
                                        is TokenType.Dynamic -> Token(tokenType, "", mutableStringRangeOf())
                                    } pipe committed::add
                                }

                                override fun commitAll(tokenTypes: Array<out TokenType>) {
                                    TODO("Not yet implemented")
                                }

                                override fun commitAll(tokenTypes: Sequence<TokenType>) {
                                    TODO("Not yet implemented")
                                }

                                override fun commitAll(tokenTypes: Collection<TokenType>) {
                                    TODO("Not yet implemented")
                                }

                                override fun commitAll(vararg tokenTypes: TokenType) {
                                    TODO("Not yet implemented")
                                }
                                override fun error(message: String): Nothing = throw CompileException(message)
                                override fun exit(): Nothing = throw Exit
                                override fun hasNext(): Boolean = next != null
                                override suspend fun next(): Char {
                                    yield()
                                    return next?:throw NoSuchElementException()
                                }
                            }.block()
                        } catch (_: Exit) {
                            exited = true
                        } catch (e: CompileException) {
                            exited = true
                            compileException = e
                        }
                    }
                }
            }
            val jobs = processes.map { it.run() }
            launch {
                while (iterator.hasNext()) {
                    next = iterator.next()
                    yield()
                }
                next = null
            }
            joinAll(*jobs.toTypedArray())
        }

        if (eof != null) tokenList.add(Token(eof))

        return@runBlocking tokenList
    }
}
