package net.loute.freem.compiler.symbol.table.frontend

//// Temporal Token Type
//enum class TTT: ITokenType {
//    PLUS,
//    A,
//    B,
//    C,
//}
//
//val tempRule =
//    flow(
//        expect(TTT.PLUS),
//        expect(TTT.A,
//            expect(TTT.B,
//                provide(TTT.C)
//            )
//        ),
//    )
//
//interface ITokenType {
//    val value: String
//}
//interface IToken {
//    val type: ITokenType
//    val value: String
//    val range: StringRange
//}
//sealed interface Item
//sealed interface ExpectItem: Item
//sealed interface FlowItem: Item
//
//class Flow(vararg items: FlowItem): ExpectItem {
//    val items = items.asList()
//}
//class Expect(val tokenType: ITokenType, val then: ExpectItem? = null): ExpectItem, FlowItem
//class ExpectWhile(val tokenType: ITokenType, val predicate: (Char) -> Boolean): ExpectItem, FlowItem
//class Provide(val tokenType: ITokenType): ExpectItem, FlowItem
//class Ok: FlowItem {
//    override fun equals(other: Any?) = this === other
//    override fun hashCode() = System.identityHashCode(this)
//}
//
//inline fun flow(vararg items: FlowItem) = Flow(*items)
//inline fun expect(tokenType: ITokenType, then: ExpectItem? = null) = Expect(tokenType, then)
//inline fun expectWhile(tokenType: ITokenType, noinline predicate: (Char) -> Boolean) = ExpectWhile(tokenType, predicate)
//inline fun provide(tokenType: ITokenType) = Provide(tokenType)
//inline fun ok() = Ok()
//
//data class StringLocation(val index: UInt, val row: UInt, val column: UInt) {
//    operator fun plus(string: String) =
//        run {
//            val split = string.lines()
//            StringLocation(
//                index + string.length.toUInt(),
//                row + (split.size - 1).toUInt(),
//                if (split.size > 1) (split.last().length - 1).toUInt()
//                else column + string.length.toUInt()
//            )
//        }
//}
//data class StringRange(val start: StringLocation, val end: StringLocation) {
//    constructor(startIndex: UInt, startRow: UInt, startColumn: UInt, endIndex: UInt, endRow: UInt, endCoulmn: UInt): this(
//        StringLocation(startIndex, startRow, startColumn), StringLocation(endIndex, endRow, endCoulmn)
//    )
//}
//
//interface ILexer
//class TokenizeOptions(val defaultTokenType: ITokenType) {
//    var createToken: (ITokenType, String, StringRange) -> IToken = { type, value, range ->
//        object: IToken {
//            override val type = type
//            override val value = value
//            override val range = range
//        }
//    }
//}
//data class TokenizeContext(val codeIter: CharIterator, val startLoc: StringLocation, val options: TokenizeOptions) {
//    fun createToken(type: ITokenType, content: String) =
//        options.createToken(type, content, createRange { it + content })
//    fun createRange(endLocProvider: (StringLocation) -> StringLocation) =
//        StringRange(startLoc, endLocProvider(startLoc))
//}
//fun ILexer.createTokenize(rule: Flow) =
//    { ctx: TokenizeContext ->
//        processItem(rule, ctx)
//    }
//data class ProcessResult(val token: IToken, val ctx: TokenizeContext)
//private fun processItem(item: Item, ctx: TokenizeContext): IToken {
//    val defaultTokenType = ctx.options.defaultTokenType
//    if (ctx.codeIter.hasNext().not()) return ctx.createToken(defaultTokenType, "\u0000")
//    when (item) {
//        is Expect ->
//    }
//    return processFlow(item)
//}/ ILexer = InterfaceLexer?/
//private fun processExpect(expect: Expect, ctx: TokenizeContext): IToken {
//    val codeIter = ctx.codeIter
//    val char = if (codeIter.hasNext()) codeIter.nextChar() else return ctx.createToken(ctx.options.defaultTokenType, "\u0000")
//    if (char)
//}
//private fun processFlow(flow: Flow, ctx: TokenizeContext, parentExpect: Expect? = null) {
//    val items = flow.items
//    val endsWithOk = items.last() is Ok
//    if (endsWithOk && parentExpect == null) throw IllegalArgumentException("'Ok' must not exist on root 'Flow'")
//    val slicedItems = if (endsWithOk) items.dropLast(1) else items
//
//    for (item in slicedItems) {
//        val tokenized = tokenize(item, ctx)
//    }
//}
