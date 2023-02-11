package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.util.string.StringLocation
import net.loute.freem.compiler.util.string.StringRange

interface ITokenType {
    val staticValue: String?
}
interface IToken {
    val type: ITokenType
    val value: String
    val range: StringRange
}

inline fun rule(vararg item: Item) = Rule(item.asList())
class Rule(val items: List<Item>)

sealed interface Item {
    fun process(ctx: TokenizeContext): ProcessResult?
}

data class ProcessResult(val token: IToken, val ctx: TokenizeContext)

inline fun expect(tokenType: ITokenType) = Expect(tokenType)
class Expect(val tokenType: ITokenType): Item {
    init {
        if (tokenType.staticValue == null) throw Error("Token type which has no static value cannot be used.")
    }
    override fun process(ctx: TokenizeContext) =
        run {
            val tokenValue = tokenType.staticValue!!
            if (ctx.code.startsWith(tokenValue).not()) null
            else ProcessResult(ctx.createToken(tokenType, tokenValue), ctx.updated(tokenValue))
        }
}

// inline fun expectIf(tokenType: ITokenType, noinline predicate: (Char) -> Boolean) =

inline fun expectWhile(tokenType: ITokenType, noinline predicate: (Char) -> Boolean) = ExpectWhile(tokenType, predicate)
class ExpectWhile(val tokenType: ITokenType, val predicate: (Char) -> Boolean): Item {
    init {
        if (tokenType.staticValue != null) throw Error("Token type which has static value cannot be used.")
    }
    override fun process(ctx: TokenizeContext) =
        run {
            val collected = ctx.code.takeWhile(predicate)
            if (collected.isEmpty()) null
            else ProcessResult(ctx.createToken(tokenType, collected), ctx.updated(collected))
        }
}

class Provide(val tokenType: ITokenType): Item {
    init {
        if (tokenType.staticValue == null) throw Error("Token type which has no static value cannot be used.")
    }
    override fun process(ctx: TokenizeContext) =
        run {
            val tokenValue = tokenType.staticValue!!
            ProcessResult(ctx.createToken(tokenType, tokenValue), ctx.updated(tokenValue))
        }
}

interface ILexer {
    val rule: Rule
    fun tokenize(ctx: TokenizeContext): List<IToken> {
        val tokens = mutableListOf<IToken>()
        var token: IToken
        var ctx = ctx
        while (true) {
            val result = process(rule, ctx) ?: break
            token = result.token
            ctx = result.ctx
            tokens.add(token)
        }
        return tokens.toList()
    }
}

fun process(rule: Rule, ctx: TokenizeContext) =
    rule.items
        .asSequence()
        .map { it.process(ctx) }
        .fold(null as ProcessResult?) { acc, it -> acc ?: it }

class TokenizeOptions(val defaultTokenType: ITokenType) {
    var createToken: (ITokenType, String, StringRange) -> IToken = { type, value, range ->
        object: IToken {
            override val type = type
            override val value = value
            override val range = range
        }
    }
}

data class TokenizeContext(val code: String, val startLoc: StringLocation, val options: TokenizeOptions) {
    fun createToken(type: ITokenType, content: String) =
        options.createToken(type, content, createRange { it + content })
    fun createRange(endLocProvider: (StringLocation) -> StringLocation) =
        StringRange(startLoc, endLocProvider(startLoc))
    fun updated(code: String, startLoc: StringLocation) =
        TokenizeContext(code, startLoc, options)
    fun updated(added: String) =
        TokenizeContext(code.substring(added.length), startLoc + added, options)
}
