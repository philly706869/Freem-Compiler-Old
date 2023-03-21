package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.symbol.table.frontend.token.FTokenType

open class Flow<C: Flow.Context<*>, R>(val flowContext: C.() -> Unit) {
    fun flow(): R {

    }


    open class Context<T> {
        sealed interface FlowItem {
            val flowItems: Array<out FlowItem>
        }
        class Or(override val flowItems: Array<out FlowItem>): FlowItem
        class Quantify(val start: Int?, val end: Int?, override val flowItems: Array<out FlowItem>): FlowItem
        class Match(override val flowItems: Array<out FlowItem>): FlowItem

        fun expect(flowItem: FlowItem) {}
        fun or(vararg flowItem: FlowItem): Or {}
        fun quantify(start: Int?, end: Int, vararg flowItem: FlowItem): Quantify {}
        fun quantify(start: Int, end: Int?, vararg flowItem: FlowItem): Quantify {}
        fun plus(vararg flowItem: FlowItem): Quantify {}
        fun star(vararg flowItem: FlowItem): Quantify {}
        fun optional(vararg flowItem: FlowItem): Quantify {}
        fun match(target: T): Match {}
        fun match(condition: (T) -> Boolean): Match {}
    }
}

open class StringFlow<C: Flow.Context<Char>>(flowContext: C.() -> Unit): Flow<C, String>(flowContext) {
    abstract class Context: Flow.Context<Char>() {
        class Alternation(override val flowItems: Array<out FlowItem>): FlowItem

        fun alternation(string: String): Alternation {}
    }
}

val testLexer = StringFlow<Flow.Context<Char>> {
    expect(
        or(

        )
    )
}

val testParser = Flow<Flow.Context<Token<FTokenType>>, Ast> {

}

//object FreemLexer: Lexer<FToken> ({
//
//})
//
//interface LexerContext<T: Token> {
//
//}
//
//typealias ScannerContextBlock<T> = suspend ScannerContext<T>.() -> Unit
//interface ScannerContext<T: Token> {
//    val lexeme: String
//    val peek: Char?
//
//    fun exit(): Nothing
//    fun error(message: String): Nothing
//
//    fun hasNext(): Boolean
//    suspend fun next(): Char
//    suspend fun nextOrNull(): Char?
//    suspend fun advance(): Char?
//
//    suspend fun skip(): Char
//    suspend fun skip(char: Char): Char?
//    suspend fun skip(vararg char: Char): Char?
//    suspend fun skip(condition: (Char) -> Boolean): Char?
//
//    suspend fun require(char: Char): Char
//    suspend fun require(vararg char: Char): Char
//    suspend fun require(condition: (Char) -> Boolean): Char
//
//    fun commit(token: T)
//}
//
//private object EXIT: Throwable()
//
//open class Lexer<T: Token>(private vararg val scanners: Scanner<T>) {
//    open fun lexicalAnalyze(sourceCode: String, pathname: String? = null): List<T>
//    = runBlocking {
//        val tokenList = mutableListOf<T>()
//        val currLoc = mutableStringLocationOf()
//        val iterator = sourceCode.iterator()
//        var currChar: Char? = null
//
//        fun createNewScannerProcess(block: suspend ImplementedScanner<T>.() -> Unit): Deferred<ProcessResult> = async {
//            with(ImplementedScanner<T>()) {
//                try { block() }
//                catch (_: EXIT) { processResult.exited = true }
//                if (lexeme.isNotEmpty()) throw IllegalStateException("you must commit left lexeme")
//                return@async processResult
//            }
//        }
//
//        val mainProcessor = launch {
//            val mainYield = ::yield
//
//            yield() // yield to update processor for initializing
//
//            while (iterator.hasNext()) {
//
//                val staticScannerProcess: Deferred<ProcessResult> = createNewScannerProcess {
//                    val lexemeBuilder = StringBuilder()
//                    var currentTrie: TrieNode = staticTrie
//                    while (hasNext()) currentTrie = currentTrie[skip().also(lexemeBuilder::append)]?:exit()
//                    processResult.committed.add(TToken(staticMap[lexemeBuilder.toString()]?:exit(), mutableStringRangeOf()))
//                }
//
//                val defers: List<Deferred<ProcessResult>> = scanners.map(::createNewScannerProcess) // create scanning processors
//
//                launch {
//                    // currentChar updater
//                    while (currChar != null && defers.any { it.isActive }) {
//                        mainYield()
//                        yield()
//                    }
//                }
//
//                val result = awaitAll(staticScannerProcess, *defers.toTypedArray())
//
//                val finalCommit = result
//                    .filter { it.exited.not() }
//                    .maxByOrNull { r ->
//                        r.committed.sumOf { it.lexeme.length }
//                    }?.committed
//                    ?: throw CompileException("character not expected: $currChar", pathname, currLoc)
//
//                tokenList.addAll(finalCommit)
//            }
//        }
//        val updateProcessor = launch {
//            while (iterator.hasNext()) {
//                val next = iterator.next()
//                currChar = next
//                currLoc.add(next)
//                yield()
//            }
//            currChar = null
//        }
//        joinAll(mainProcessor, updateProcessor)
//
//        return@runBlocking tokenList
//    }
//}
//
////object FreemLexer: Lexer(
////    {
////        scanner { while (peek?.isWhitespace() == true) skip() }
//////        scanner { // identifier
//////            require { it.isAlpha() || it == '_' }
//////            while (hasNext()) require { it.isAlpha() || it.isDigit() || it == '_' }
//////            commit(TokenTypes.IDENTIFIER)
//////        }
//////        scanner { // literal
//////            require(Char::isDigit)
//////            suspend fun whileDigit() { while (peek?.isDigit() == true) advance() }
//////            whileDigit()
//////            if (peek == '.') {
//////                whileDigit()
//////
//////            } else {
//////
//////            }
//////        }
//////        scanner {
//////            //
//////        }
////    },
////    *TokenTypes.Separator.values(),
////    *TokenTypes.Operator.values(),
////    *TokenTypes.Keyword.values()
////)
////
////typealias ScannerBlock = suspend Lexer.Scanner.() -> Unit
////
////interface ScannerBuilder { fun scanner(scanner: ScannerBlock) }
////
////open class Lexer(dynamicBuilderBlock: ScannerBuilder.() -> Unit, vararg statics: TokenType.Static) {
////    private val scanners: List<ScannerBlock> = object: ScannerBuilder {
////        val dynamics: MutableList<ScannerBlock> = mutableListOf()
////        override fun scanner(scanner: ScannerBlock) { dynamics.add(scanner) }
////    }.apply(dynamicBuilderBlock).dynamics
////    private val staticTrie: Trie = statics.map { it.staticValue }.toTrie()
////    private val staticMap: Map<String, TokenType.Static> = statics.filter { it.staticValue.isNotEmpty() }.associateBy { it.staticValue }
////
////    interface Scanner {
////        val lexeme: String
////        val peek: Char?
////
////        fun exit(): Nothing
////        fun error(message: String): Nothing
////
////        fun hasNext(): Boolean
////        suspend fun next(): Char
////        suspend fun nextOrNull(): Char?
////        suspend fun advance(): Char?
////
////        suspend fun skip(): Char
////        suspend fun skip(char: Char): Char?
////        suspend fun skip(vararg char: Char): Char?
////        suspend fun skip(condition: (Char) -> Boolean): Char?
////
////        suspend fun require(char: Char): Char
////        suspend fun require(vararg char: Char): Char
////        suspend fun require(condition: (Char) -> Boolean): Char
////
////        fun commit(tokenType: TokenType.Dynamic)
////    }
////
////    private object Exit: Throwable()
////    private interface ProcessResult {
////        val exited: Boolean
////        val committed: List<Token>
////    }
////    private class ProcessResultBuilder: ProcessResult {
////        override var exited: Boolean = false
////        override val committed: MutableList<Token> = mutableListOf()
////    }
////
////    open fun lexicalAnalyse(sourceCode: String, pathname: String? = null): List<Token> = runBlocking {
////        val iterator = sourceCode.iterator()
////        val tokenList = mutableListOf<Token>()
////        val currLoc = mutableStringLocationOf()
////
////        var currChar: Char? = null
////        class ImplementedScanner: Scanner {
////            val lexemeBuilder = StringBuilder()
////            val processResult = ProcessResultBuilder()
////            override val lexeme: String get() = lexemeBuilder.toString()
////
////            override val peek: Char? get() = currChar
////
////            override fun error(message: String): Nothing = throw CompileException(message, pathname, null)
////            override fun exit(): Nothing = throw Exit
////
////            private suspend inline fun updateChar() = yield()
////
////            override fun hasNext(): Boolean = currChar != null
////            override suspend fun next(): Char = nextOrNull() ?: throw NoSuchElementException()
////            override suspend fun nextOrNull(): Char? {
////                updateChar()
////                return peek?.also(lexemeBuilder::append)
////            }
////
////            override suspend fun advance(): Char {
////                val before = peek ?: throw NoSuchElementException()
////                nextOrNull()
////                return before
////            }
////
////            override suspend fun skip(): Char {
////                if (lexeme.isNotEmpty()) throw IllegalStateException("lexeme is not empty")
////                val before = peek ?: throw NoSuchElementException()
////                updateChar()
////                return before
////            }
////            override suspend fun skip(char: Char): Char? = if (peek == char) skip() else null
////            override suspend fun skip(vararg char: Char): Char? = char.find { it == peek }?.also { skip() }
////            override suspend fun skip(condition: (Char) -> Boolean): Char? = if (peek != null && condition(peek!!)) skip() else null
////
////            override suspend fun require(char: Char): Char = if (advance() != char) exit() else char
////            override suspend fun require(vararg char: Char): Char {
////                val nextChar = nextOrNull() ?: exit()
////                return char.find { it == nextChar } ?: exit()
////            }
////            override suspend fun require(condition: (Char) -> Boolean): Char {
////                val nextChar = nextOrNull() ?: exit()
////                if (condition(nextChar).not()) exit()
////                return nextChar
////            }
////
////            override fun commit(tokenType: TokenType.Dynamic) { processResult.committed.add(Token(tokenType, lexeme, mutableStringRangeOf())) }
////        }
////
////        fun createNewScannerProcess(block: suspend ImplementedScanner.() -> Unit): Deferred<ProcessResult> = async {
////            with(ImplementedScanner()) {
////                try { block() }
////                catch (_: Exit) { processResult.exited = true }
////                if (lexeme.isNotEmpty()) throw IllegalStateException("you must commit left lexeme")
////                return@async processResult
////            }
////        }
////
////        val mainProcessor = launch {
////            val mainYield = ::yield
////
////            yield() // yield to update processor for initializing
////
////            while (iterator.hasNext()) {
////
////                val staticScannerProcess: Deferred<ProcessResult> = createNewScannerProcess {
////                    val lexemeBuilder = StringBuilder()
////                    var currentTrie: TrieNode = staticTrie
////                    while (hasNext()) currentTrie = currentTrie[skip().also(lexemeBuilder::append)]?:exit()
////                    processResult.committed.add(Token(staticMap[lexemeBuilder.toString()]?:exit(), mutableStringRangeOf()))
////                }
////
////                val defers: List<Deferred<ProcessResult>> = scanners.map(::createNewScannerProcess) // create scanning processors
////
////                launch {
////                    // currentChar updater
////                    while (currChar != null && defers.any { it.isActive }) {
////                        mainYield()
////                        yield()
////                    }
////                }
////
////                val result = awaitAll(staticScannerProcess, *defers.toTypedArray())
////
////                val finalCommit = result
////                    .filter { it.exited.not() }
////                    .maxByOrNull { r ->
////                        r.committed.sumOf { it.lexeme.length }
////                    }?.committed
////                    ?: throw CompileException("character not expected: $currChar", pathname, currLoc)
////
////                tokenList.addAll(finalCommit)
////            }
////        }
////        val updateProcessor = launch {
////            while (iterator.hasNext()) {
////                val next = iterator.next()
////                currChar = next
////                if (next == '\n') {
////                    currLoc.column = 0
////                    currLoc.row++
////                }
////                currLoc.index++
////                yield()
////            }
////            currChar = null
////        }
////        joinAll(mainProcessor, updateProcessor)
////
////        return@runBlocking tokenList
////    }
////}
