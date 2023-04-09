package net.loute.freem.compiler.util

import net.loute.freem.compiler.symbol.table.frontend.token.FTokenType
import net.loute.freem.compiler.symbol.table.frontend.token.Token
import net.loute.freem.compiler.util.collection.Tree

abstract class Flow<R> {
    abstract fun flow(): R
    //protected abstract fun next()
}

typealias AnyFlowItem = FlowItem<*>

sealed interface FlowItem<T> { val content: T }
data class Or(override val content: List<AnyFlowItem>): FlowItem<List<AnyFlowItem>>
data class Quantify(val min: UInt, val max: UInt?, override val content: AnyFlowItem): FlowItem<AnyFlowItem> {
    init { if (max != null && min >= max) throw IllegalArgumentException("min must be less than max") }
}
fun or(vararg content: AnyFlowItem): Or = Or(content.toList())
fun quantify(min: Int, max: Int?, content: AnyFlowItem): Quantify = Quantify(min.toUInt(), max?.toUInt(), content)
fun quantify(range: IntRange, content: AnyFlowItem): Quantify = Quantify(range.first.toUInt(), range.last.toUInt(), content)
fun quantity(range: UIntRange, content: AnyFlowItem): Quantify = Quantify(range.first, range.last, content)
fun plus(content: AnyFlowItem): Quantify = Quantify(1u, null, content)
fun star(content: AnyFlowItem): Quantify = Quantify(0u, null, content)
fun optional(content: AnyFlowItem): Quantify = Quantify(0u, 1u, content)
abstract class Match<T>(override val content: T): FlowItem<T>

//open class TreeFlow<T>(protected open val content: ): Flow<Tree<T>>() {
//    override fun flow(): Tree<T> {
//        TODO("Not yet implemented")
//    }
//}
//
//open class ListFlow<T>(protected open val context: FlowContextBlock<T>): Flow<List<T>>() {
//    override fun flow(): List<T> {
//
//    }
//}


/*open class Flow<C, R>(val flowContext: Context<C>.() -> Unit) {
    fun flow(): R {
        TODO("Not yet implemented")
    }

    interface Context<T> {
        sealed interface FlowItem {
            val flowItems: Array<out FlowItem>
        }
        class Or(override val flowItems: Array<out FlowItem>): FlowItem
        class Quantify(val start: Int?, val end: Int?, override val flowItems: Array<out FlowItem>): FlowItem
        class Match(override val flowItems: Array<out FlowItem>): FlowItem

        fun expect(flowItem: FlowItem)
        fun or(vararg flowItem: FlowItem): Or
        fun quantify(start: Int?, end: Int, vararg flowItem: FlowItem): Quantify
        fun quantify(start: Int, end: Int?, vararg flowItem: FlowItem): Quantify
        fun plus(vararg flowItem: FlowItem): Quantify
        fun star(vararg flowItem: FlowItem): Quantify
        fun optional(vararg flowItem: FlowItem): Quantify
        fun match(target: T): Match
        fun match(condition: (T) -> Boolean): Match
    }
}

open class StringFlow(flowContext: Context.() -> Unit): Flow<Char, String>(flowContext) {
    abstract class Context: Flow.Context<Char> {
        class Alternation(override val flowItems: Array<out Flow.Context.FlowItem>): FlowItem

        fun alternation(string: String): Alternation {}
    }
}*/