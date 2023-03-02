package net.loute.freem.compiler.symbol.table.frontend

import kotlinx.coroutines.*
import net.loute.freem.compiler.CompileException
import net.loute.freem.compiler.symbol.table.frontend.token.*
import net.loute.freem.compiler.util.collection.Trie
import net.loute.freem.compiler.util.collection.TrieNode
import net.loute.freem.compiler.util.collection.toTrie
import net.loute.freem.compiler.util.isAlpha
import net.loute.freem.compiler.util.pipe
import net.loute.freem.compiler.util.range.mutableStringRangeOf

object FreemLexer: Lexer({
    addDynamic { // identifier
        require { it.isAlpha() || it == '_' }
        while (hasNext()) require { it.isAlpha() || it.isDigit() || it == '_' }
        commit(TokenTypes.IDENTIFIER)
    }
    addDynamic { // literal
        require(Char::isDigit)
        suspend fun whileDigit() { while (peek?.isDigit() == true) next() }
        whileDigit()
        if (peek == '.') {
            whileDigit()

        }
        else {

        }
    }
    addDynamic {
        //
    }
    addStatic(
        *TokenTypes.Separator.values(),
        *TokenTypes.Operator.values(),
        *TokenTypes.Keyword.values()
    )
    eof = TokenTypes.EOF
})

private typealias ScannerBlock = suspend Lexer.Scanner.() -> Unit

interface LexerBuilder {
    fun addStatic(tokenTypes: Array<out TokenType.Static>)
    fun addStatic(tokenTypes: Sequence<TokenType.Static>)
    fun addStatic(tokenTypes: Collection<TokenType.Static>)
    fun addStatic(vararg tokenTypes: TokenType.Static)
    fun addDynamic(scanner: ScannerBlock)
    var eof: TokenType.Abstract?
}

open class Lexer(lexerBuilderBlock: LexerBuilder.() -> Unit) {
    private val scanners: List<ScannerBlock>
    private val eof: TokenType.Abstract?

    init {
        val lexerBuilder = object: LexerBuilder {
            val statics: MutableList<TokenType.Static> = mutableListOf()
            val dynamics: MutableList<ScannerBlock> = mutableListOf()
            override var eof: TokenType.Abstract? = null

            override fun addStatic(tokenTypes: Array<out TokenType.Static>) { statics.addAll(tokenTypes) }
            override fun addStatic(tokenTypes: Sequence<TokenType.Static>) { statics.addAll(tokenTypes) }
            override fun addStatic(tokenTypes: Collection<TokenType.Static>) { statics.addAll(tokenTypes) }
            override fun addStatic(vararg tokenTypes: TokenType.Static) { statics.addAll(tokenTypes) }
            override fun addDynamic(scanner: ScannerBlock) { dynamics.add(scanner) }
        }

        val scanners: MutableList<ScannerBlock> = lexerBuilder.dynamics

        if (lexerBuilder.statics.isNotEmpty() && lexerBuilder.statics.any { it.staticValue.isEmpty() }.not()) {
            val staticTrie: Trie = lexerBuilder.statics.map { it.staticValue }.toTrie()
            val staticMap: Map<String, TokenType.Static> = lexerBuilder.statics.associateBy { it.staticValue }
            val staticProcessBlock: ScannerBlock = {
                var currentTrie: TrieNode = staticTrie
                while (hasNext()) currentTrie = currentTrie[next()]?:exit()
                commit(staticMap[lexeme]?:exit())
            }
            scanners.add(0, staticProcessBlock)
        }

        this.scanners = scanners
        this.eof = lexerBuilder.eof
    }

    interface Scanner {
        val lexeme: String
        val peek: Char?

        fun exit(): Nothing
        fun error(message: String): Nothing

        fun hasNext(): Boolean
        suspend fun next(): Char
        suspend fun nextOrNull(): Char?
        suspend fun nextOrExit(): Char

        suspend fun advance(): Char?

        suspend fun require(char: Char): Char
        suspend fun require(vararg char: Char): Char
        suspend fun require(charRange: CharRange): Char
        suspend fun require(condition: (Char) -> Boolean): Char

        fun commit(tokenType: TokenType)
        fun commitAll(tokenTypes: Array<out TokenType>)
        fun commitAll(tokenTypes: Sequence<TokenType>)
        fun commitAll(tokenTypes: Collection<TokenType>)
        fun commitAll(vararg tokenTypes: TokenType)
    }

    private object Exit: Throwable()
    private interface ProcessResult {
        val exited: Boolean
        val compileException: CompileException?
        val committed: List<Token>
    }
    private class ProcessResultBuilder: ProcessResult {
        override var exited: Boolean = false
        override var compileException: CompileException? = null
        override val committed: MutableList<Token> = mutableListOf()
    }

    fun lexicalAnalyse(sourceCode: String, pathname: String? = null): List<Token> = lexicalAnalyse(sourceCode.iterator(), pathname)
    fun lexicalAnalyse(iterator: CharIterator, pathname: String? = null): List<Token> = runBlocking {
        val tokenList = mutableListOf<Token>()
        val currentRange = mutableStringRangeOf()

        var currentChar: Char? = null
        class ImplementedScanner: Scanner {
            val lexemeBuilder = StringBuilder()
            val processResult = ProcessResultBuilder()
            override val lexeme: String get() = lexemeBuilder.toString()

            override val peek: Char? get() = currentChar

            override fun error(message: String): Nothing = throw CompileException(message)
            override fun exit(): Nothing = throw Exit

            override fun hasNext(): Boolean = currentChar != null

            override suspend fun next(): Char { yield(); return peek ?: throw NoSuchElementException() }
            override suspend fun nextOrNull(): Char? { yield(); return peek }
            override suspend fun nextOrExit(): Char = nextOrNull() ?: exit()

            override suspend fun advance(): Char? {
                val before = peek
                yield()
                return before
            }

            override suspend fun require(char: Char): Char {
                return if (advance() != char) exit() else char
            }

            override suspend fun require(vararg char: Char): Char {
                val nextChar = nextOrExit()
                return char.find { it == nextChar } ?: exit()
            }

            override suspend fun require(charRange: CharRange): Char {
                val nextChar = nextOrExit()
                if (nextChar !in charRange) exit()
                return nextChar
            }

            override suspend fun require(condition: (Char) -> Boolean): Char {
                val nextChar = nextOrExit()
                if (condition(nextChar).not()) exit()
                return nextChar
            }

            override fun commit(tokenType: TokenType) {
                when (tokenType) {
                    is TokenType.Abstract -> Token(tokenType)
                    is TokenType.Static -> Token(tokenType, mutableStringRangeOf())
                    is TokenType.Dynamic -> Token(tokenType, "", mutableStringRangeOf())
                } pipe processResult.committed::add
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
        }

        fun createNewScannerProcess(block: ScannerBlock): Deferred<ProcessResult> = async {
            with(ImplementedScanner()) {
                try {
                    block()
                } catch (_: Exit) {
                    processResult.exited = true
                } catch (compileException: CompileException) {
                    processResult.exited = true
                    processResult.compileException = compileException
                }
                if (lexemeBuilder.toString().isNotEmpty()) throw IllegalStateException("you must commit left lexeme")
                processResult
            }
        }

        val mainProcessor = launch {
            val mainYield = ::yield
            yield() // yield to update processor for initializing
            while (iterator.hasNext()) {
                val defers = scanners.map(::createNewScannerProcess) // create scanning processors
                launch { // currentChar updater
                    while (currentChar != null && defers.any { it.isActive }) {
                        mainYield()
                        yield()
                    }
                }
                val result = awaitAll(*defers.toTypedArray())

                val errorCount = result.count { it.compileException != null }
                val buffer = result.maxBy { r -> r.committed.filterIsInstance<Token.InlineToken>().sumOf { it.lexeme.length } }
            }
        }
        val updateProcessor = launch {
            while (iterator.hasNext()) {
                currentChar = iterator.next()
                yield()
            }
            currentChar = null
        }
        joinAll(mainProcessor, updateProcessor)

        if (eof != null) tokenList.add(Token(eof))

        return@runBlocking tokenList
    }
}