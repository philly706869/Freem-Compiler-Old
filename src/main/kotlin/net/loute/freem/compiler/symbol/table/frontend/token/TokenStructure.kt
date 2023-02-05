package net.loute.freem.compiler.symbol.table.frontend.token

sealed interface ITokenType
interface IStaticTokenType: ITokenType { val value: String }
interface IDynamicTokenType: ITokenType { val structure: TokenStructure }

data class TokenTypeSet(val tokenTypes: List<ITokenType>) {
    constructor(vararg type: ITokenType): this(type.toList())
    constructor(type: Array<out ITokenType>): this(type.toList())
    constructor(type: Iterable<ITokenType>): this(type.toList())
}

typealias TokenTypeSetBuilderBlock = TokenTypeSetBuilder.() -> Unit
class TokenTypeSetBuilder private constructor(builderBlock: TokenTypeSetBuilderBlock) {
    init { this.builderBlock() }
    companion object {
        operator fun invoke(builderBlock: TokenTypeSetBuilderBlock) = TokenTypeSetBuilder(builderBlock).build()
    }

    private fun build() = TokenTypeSet(types.toList())
    private val types = mutableListOf<ITokenType>()

    fun add(type: ITokenType) = types.add(type)
    fun add(vararg type: ITokenType) = types.addAll(type)
    fun add(type: Array<out ITokenType>) = types.addAll(type)
    fun add(type: Collection<ITokenType>) = types.addAll(type)
    fun add(type: Iterable<ITokenType>) = types.addAll(type)
    fun add(type: Sequence<ITokenType>) = types.addAll(type)
}

inline fun tokenTypeSetOf(noinline tokenSetBuilderBlock: TokenTypeSetBuilderBlock) = TokenTypeSetBuilder(tokenSetBuilderBlock)
fun tokenTypeSetOf(vararg type: ITokenType) = TokenTypeSet(type)
fun tokenTypeSetOf(types: Array<out ITokenType>) = TokenTypeSet(types)
fun tokenTypeSetOf(types: Iterable<ITokenType>) = TokenTypeSet(types)

data class TokenStructure(val items: List<TokenStructureItem<*>>)

//typealias TokenStructureBuilderBlock = TokenStructureBuilder.() -> Unit
//class TokenStructureBuilder private constructor(builderBlock: TokenStructureBuilderBlock) {
//    init { this.builderBlock() }
//    companion object {
//        operator fun invoke(builderBlock: TokenStructureBuilderBlock) = TokenStructureBuilder(builderBlock).build()
//    }
//    private fun build() =
//}

fun tokenStructureOf(vararg items: TokenStructureItem<*>) = TokenStructure(items.toList())
fun tokenStructureOf(items: Array<out TokenStructureItem<*>>) = TokenStructure(items.toList())
fun tokenStructureOf(items: Iterable<TokenStructureItem<*>>) = TokenStructure(items.toList())

sealed interface TokenStructureItem<T> { val content: T }

data class Text(override val content: String): TokenStructureItem<String>
data class Or(override val content: List<TokenStructureItem<*>>): TokenStructureItem<List<TokenStructureItem<*>>>
data class Amount(val min: Int, val max: Int?, override val content: List<TokenStructureItem<*>>): TokenStructureItem<List<TokenStructureItem<*>>>
data class Condition(override val content: (Char) -> Boolean): TokenStructureItem<(Char) -> Boolean>

fun text(content: String) = Text(content)
fun or(vararg content: TokenStructureItem<*>) = Or(content.toList())
fun plus(vararg content: TokenStructureItem<*>) = Amount(1, null, content.toList())
fun star(vararg content: TokenStructureItem<*>) = Amount(0, null, content.toList())
fun amount(min: Int, max: Int?, vararg content: TokenStructureItem<*>) = Amount(min, max, content.toList())
fun contain(content: CharRange) = condition { it in content }
inline fun condition(noinline content: (Char) -> Boolean) = Condition(content)

inline infix fun TokenStructureItem<*>.or(item: TokenStructureItem<*>) = or(this, item)
inline infix fun TokenStructureItem<*>.or(or: Or) = Or(listOf(this) + or.content)
inline infix fun Or.or(item: TokenStructureItem<*>) = Or(content + item)
inline infix fun Or.or(or: Or) = Or(content + or.content)

val smallLetter     by lazy { condition(Char::isLowerCase) }
val capitalLetter   by lazy { condition(Char::isUpperCase) }
val letter          by lazy { condition(Char::isLetter) }
val letter_         by lazy { condition(Char::isLetter) or text("_") }
val digit           by lazy { condition(Char::isDigit) }
val word            by lazy { condition(Char::isLetterOrDigit) or text("_") }
val whiteSpace      by lazy { condition(Char::isWhitespace) }

//data class Token(val type: ITokenType, val lexeme: String, val range: StringRange)
