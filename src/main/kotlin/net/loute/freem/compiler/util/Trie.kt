package net.loute.freem.compiler.util

import java.io.Serializable

fun main() {
    val a = mutableListOf(1, 2, 3)
    val b = mutableListOf(1, 2, 3, 4)
    println(a.retainAll(b))
    println(a)
}

class TrieNode(isLeaf: Boolean, children: LinkedHashMap<Char, TrieNode>?) {
    var isLeaf: Boolean = isLeaf
        private set
    var children: LinkedHashMap<Char, TrieNode>? = children
        private set

    override fun toString(): String = "TrieNode(isLeaf=$isLeaf, children=$children)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrieNode

        if (isLeaf != other.isLeaf) return false
        if (children != other.children) return false

        return true
    }
    override fun hashCode(): Int {
        var result = isLeaf.hashCode()
        result = 31 * result + (children?.hashCode() ?: 0)
        return result
    }
}

private object EmptyTrie: Trie, Serializable {
    private const val serialVersionUID: Long = 5736128953612847639

    override fun equals(other: Any?): Boolean = other is Trie && other.isEmpty()
    override fun hashCode(): Int = 0
    override fun toString(): String = "{}"

    override val root: TrieNode = TrieNode(false, null)
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

interface Trie: Collection<String> {
    val root: TrieNode
}
interface MutableTrie: Trie, MutableCollection<String>

class HashTrie: MutableTrie {
    override val root: TrieNode = TrieNode(false, null)
    override val size: Int get() = innerSize
    private var innerSize: Int = 0

    constructor()
    constructor(elements: Collection<String>) { addAll(elements) }

    override fun contains(element: String): Boolean {
        var current = root
        for (char in element) current = current.children?.get(char)?:return false
        return current.isLeaf
    }

    override fun containsAll(elements: Collection<String>): Boolean {
        val thisContain = this::contains
        return elements.any { thisContain(it).not() }
    }

    override fun isEmpty(): Boolean = !root.isLeaf && root.children == null

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

    override fun add(element: String): Boolean {
        val new = { TrieNode(false, null) }
        var current = root
        for (char in element) {
            if (current.children == null) current.children = LinkedHashMap(4, 0.75f)
            current = current.children!!.getOrPut(char, new)
        }
        return current.isLeaf.not().then {
            current.isLeaf = true
            innerSize++
        }
    }

    override fun addAll(elements: Collection<String>): Boolean {
        val thisAdd = this::add
        return elements.any { thisAdd(it).not() }
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun remove(element: String): Boolean {
        var current = root
        for (char in element) current = current.children?.get(char)?:return false
        if (current.isLeaf.not()) return false

    }

    override fun removeAll(elements: Collection<String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements: Collection<String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "{ root=$root }"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HashTrie

        if (root != other.root) return false
        if (size != other.size) return false

        return true
    }
    override fun hashCode(): Int {
        var result = root.hashCode()
        result = 31 * result + size
        return result
    }
}


//class Trie private constructor(private val root: TrieNode): Collection<String> {
//    constructor(words: Array<out String>): this(
//        kotlin.run {
//            val root = TrieNode(false)
//            for (word in words) {
//                var current = root
//                for (char in word) {
//                    val new = { TrieNode(false) }
//                    current = current.children?.getOrPut(char, new)?:new()
//                }
//                current.isLeaf = true
//            }
//            root
//        }
//    )
//    constructor(words: Iterable<String>): this(words.toList().toTypedArray())
//    constructor(words: Sequence<String>): this(words.toList().toTypedArray())
//
//    override val size: Int = kotlin.run {
//        fun scan(node: TrieNode): Int {
//            var size = 0
//            if (node.isLeaf) size++
//            node.children?.forEach { node -> size += scan(node.value) }
//            return size
//        }
//        scan(root)
//    }
//
//    private val isEmpty = root.children == null && !root.isLeaf
//    override fun isEmpty(): Boolean = isEmpty
//
//    override fun iterator(): Iterator<String> =
//        object: Iterator<String> {
//            val stack = Stack<Pair<Char, TrieNode>>()
//
//            override fun hasNext(): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun next(): String {
//                TODO("Not yet implemented")
//                return stack.map { it.first }.joinToString("")
//            }
//        }
//
//    override fun containsAll(words: Collection<String>): Boolean = words.any { word -> contains(word).not() }.not()
//
//    override fun contains(word: String): Boolean {
//        var current = root
//        for (char in word) {
//            current = current.children?.get(char)?:return false
//        }
//        return current.isLeaf
//    }
//}
//
//class MutableTrie: MutableCollection<String> {
//    constructor(words: Array<out String>): this(
//        kotlin.run {
//            val root = TrieNode(false)
//            for (word in words) {
//                var current = root
//                for (char in word) {
//                    val new = { TrieNode(false) }
//                    current = current.children?.getOrPut(char, new)?:new()
//                }
//                current.isLeaf = true
//            }
//            root
//        }
//    )
//    constructor(words: Iterable<String>): this(words.toList().toTypedArray())
//    constructor(words: Sequence<String>): this(words.toList().toTypedArray())
//
//    override var size: Int = kotlin.run {
//        fun scan(node: TrieNode): Int {
//            var size = 0
//            if (node.isLeaf) size++
//            node.children?.forEach { node -> size += scan(node.value) }
//            return size
//        }
//        scan(root)
//    }
//        private set
//
//    override fun clear() {
//        TODO("Not yet implemented")
//    }
//
//    override fun addAll(elements: Collection<String>): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun add(element: String): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun isEmpty(): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun iterator(): MutableIterator<String> {
//        TODO("Not yet implemented")
//    }
//
//    override fun retainAll(elements: Collection<String>): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeAll(elements: Collection<String>): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun remove(element: String): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun containsAll(elements: Collection<String>): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun contains(element: String): Boolean {
//        TODO("Not yet implemented")
//    }
//
//}

/*
fun Array<out String>.toTrie() = Trie(this)
fun Iterable<String>.toTrie() = Trie(this)
fun Sequence<String>.toTrie() = Trie(this)

fun trieOf(vararg word: String) = Trie(word)

class Trie(val isCompleted: Boolean, val children: Map<Char, Trie>) {
    constructor(words: Array<out String>): this(words.asSequence())
    constructor(words: Iterable<String>): this(words.asSequence())
    constructor(words: Sequence<String>): this(
        words.any { it.isEmpty() },
        words
            .filter { it.isNotEmpty() }
            .map {
                val first = it.first()
                val rest = it.drop(1)
                first to rest
            }
            .fold(mutableMapOf<Char, MutableList<String>>()) { map, (first, rest) ->
                if (first in map) map[first]!!.add(rest)
                else map[first] = mutableListOf(rest)
                map
            }
            .mapValues { (_, rests) ->
                Trie(rests)
            }
    )

    operator fun contains(char: Char): Boolean = char in children
    operator fun contains(word: String): Boolean =
        word.firstOrNull()?.let {
            it in this && word.drop(1) !in children[it]!!
        } ?: this.isCompleted
    fun containsCompleted(word: String): Boolean =
        this.isCompleted.not() && word.isNotEmpty() && children[word.first()]?.let { child ->
            val excludeFirst = word.drop(1)
            excludeFirst.isEmpty() && (child.isCompleted || child.containsCompleted(excludeFirst))
        } == true
    operator fun get(char: Char) =
        children[char]
    operator fun plus(other: Trie): Trie =
        Trie(
            this.isCompleted || other.isCompleted,
            (other.children + children)
                .map { (char, child) ->
                    char to (
                            // if character is contained in both Tries, combine their values
                            if (char in other.children) child + other.children[char]!!
                            else child
                            )
                }
                .toMap()
        )
    override fun toString() =
        run {
            val components = mutableListOf<String>()
            if (this.isCompleted) components += "completed"
            val childrenJoined = children.map { (k, v) -> "$k=$v" }.joinToString(", ")
            if (childrenJoined.isNotEmpty()) components += childrenJoined
            "Trie{${components.joinToString(", ")}}"
        }
}
*/
