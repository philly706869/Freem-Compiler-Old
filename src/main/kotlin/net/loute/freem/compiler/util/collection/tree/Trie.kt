package net.loute.freem.compiler.util.collection.tree

import java.io.Serializable
import java.util.*
import kotlin.collections.LinkedHashMap

fun Array<out String>.toTrie(): Trie = HashTrie(toList())
fun Collection<String>.toTrie(): Trie = HashTrie(this)
fun Sequence<String>.toTrie(): Trie = HashTrie(toList())
fun Array<out String>.toMutableTrie(): MutableTrie = HashTrie(toList())
fun Collection<String>.toMutableTrie(): MutableTrie = HashTrie(this)
fun Sequence<String>.toMutableTrie(): MutableTrie = HashTrie(toList())

private object EmptyTrie: Trie, Serializable {
    private const val serialVersionUID: Long = 5736128953612847639

    override fun equals(other: Any?): Boolean = other is Trie && other.isEmpty()
    override fun hashCode(): Int = 0
    override fun toString(): String = "{ isLeaf=false, children=null }"

    override val isLeaf: Boolean = false
    override val children: Map<Char, TrieNode>? = null
    override val size: Int = 0

    override fun contains(element: String): Boolean = false
    override fun containsAll(elements: Collection<String>): Boolean = false
    override fun isEmpty(): Boolean = true
    override fun iterator(): Iterator<Nothing> = iterator()
    private fun readResolve(): Any = EmptyTrie
}

fun emptyTrie(): Trie = EmptyTrie

inline fun trieOf(): Trie = emptyTrie()
fun trieOf(vararg word: String): Trie = if (word.isNotEmpty()) HashTrie(word.asList()) else emptyTrie()
fun mutableTrieOf(): MutableTrie = HashTrie()
fun mutableTrieOf(vararg word: String): MutableTrie = HashTrie(word.asList())

interface Trie: TrieNode, Collection<String> {
    override val children: Map<Char, TrieNode>?
    override operator fun get(char: Char) = children?.get(char)
}
interface MutableTrie: Trie, MutableCollection<String>

interface TrieNode {
    val isLeaf: Boolean
    val children: Map<Char, TrieNode>?
    operator fun get(char: Char) = children?.get(char)
}
private interface MutableTrieNode: TrieNode {
    override var isLeaf: Boolean
    override var children: MutableMap<Char, MutableTrieNode>?
}

open class HashTrie: MutableTrie {

    constructor()
    constructor(elements: Collection<String>) { addAll(elements) }

    override val isLeaf: Boolean get() = false
    override val children: Map<Char, TrieNode>? get() = innerChildren
    private var innerChildren: MutableMap<Char, MutableTrieNode>? = null
    override val size: Int get() = innerSize
    private var innerSize: Int = 0

    override fun contains(element: String): Boolean {
        var current: TrieNode = this
        for (char in element) current = current.children?.get(char)?:return false
        return current.isLeaf
    }

    override fun containsAll(elements: Collection<String>): Boolean = elements.any { this@HashTrie.contains(it).not() }

    override fun isEmpty(): Boolean = children == null

    override fun iterator(): MutableIterator<String> =
        object: MutableIterator<String> {
            override fun hasNext(): Boolean {
                TODO("Not yet implemented")
            }

            override fun next(): String {
                TODO("Not yet implemented")
            }

            override fun remove() {
                TODO("Not yet implemented")
            }
        }

    private val thisAsMutableTrieNode: MutableTrieNode by lazy {
        object: MutableTrieNode {
            override var isLeaf: Boolean
                get() = false
                set(_) {}
            override var children: MutableMap<Char, MutableTrieNode>?
                get() = this@HashTrie.innerChildren
                set(value) { this@HashTrie.innerChildren = value }
            override fun toString(): String = "{ isLeaf=$isLeaf, children=$children }"
        }
    }

    override fun add(element: String): Boolean {
        if (element.isEmpty()) return false
        var current = thisAsMutableTrieNode
        for (char in element) {
            if (current.children == null) current.children = LinkedHashMap(4)
            current = current.children!!.getOrPut(char) {
                object: MutableTrieNode {
                    override var isLeaf: Boolean = false
                    override var children: MutableMap<Char, MutableTrieNode>? = null
                    override fun toString(): String = "{ isLeaf=$isLeaf, children=$children }"
                }
            }
        }
        return if (current.isLeaf.not()) {
            current.isLeaf = true
            innerSize++
            true
        } else false
    }

    override fun addAll(elements: Collection<String>): Boolean = elements.any { this@HashTrie.add(it).not() }

    override fun clear() {
        innerChildren = null // 메모리 누수 확인 안함
        innerSize = 0
    }

    override fun remove(element: String): Boolean {
        if (element.isEmpty()) return false
        val currentStack = Stack<MutableTrieNode>()
        currentStack += thisAsMutableTrieNode
        for (char in element) currentStack += currentStack.last().children?.get(char)?:return false
        val last = currentStack.last()
        if (last.isLeaf.not()) return false
        last.isLeaf = false
        if (last.children == null) {
            while (currentStack.last().isLeaf.not()) {
                currentStack.pop()
                currentStack.last().children = null
            }
        }
        return true
    }

    override fun removeAll(elements: Collection<String>): Boolean = elements.any { this@HashTrie.remove(it).not() }

    override fun retainAll(elements: Collection<String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "${children?:"{}"}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HashTrie

        if (children != other.children) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = children?.hashCode() ?: 0
        result = 31 * result + size
        return result
    }
}