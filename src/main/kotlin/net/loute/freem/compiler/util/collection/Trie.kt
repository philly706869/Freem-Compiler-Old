package net.loute.freem.compiler.util.collection

fun Array<out String>.toTrie(): Trie = HashTrie(toList())
fun Collection<String>.toTrie(): Trie = HashTrie(this)
fun Sequence<String>.toTrie(): Trie = HashTrie(toList())
fun Array<out String>.toMutableTrie(): MutableTrie = HashTrie(toList())
fun Collection<String>.toMutableTrie(): MutableTrie = HashTrie(this)
fun Sequence<String>.toMutableTrie(): MutableTrie = HashTrie(toList())

private object EmptyTrie: Trie, java.io.Serializable {
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

interface Trie: Collection<String>, TrieNode
interface MutableTrie: Trie, MutableCollection<String>

interface TrieNode {
    val isLeaf: Boolean
    val children: Map<Char, TrieNode>?
    operator fun get(char: Char) = children?.get(char)
}
interface MutableTrieNode: TrieNode {
    override var isLeaf: Boolean
    override var children: MutableMap<Char, MutableTrieNode>?
}

class HashTrie: MutableTrie {

    constructor()
    constructor(elements: Collection<String>) { addAll(elements) }

    override val isLeaf: Boolean get() = innerIsLeaf
    private var innerIsLeaf: Boolean = false
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

    override fun isEmpty(): Boolean = !isLeaf && children == null

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

    private val thisAsMutableTrieNode: MutableTrieNode =
        object: MutableTrieNode {
            override var isLeaf: Boolean
                get() = this@HashTrie.innerIsLeaf
                set(value) { this@HashTrie.innerIsLeaf = value }
            override var children: MutableMap<Char, MutableTrieNode>?
                get() = this@HashTrie.innerChildren
                set(value) { this@HashTrie.innerChildren = value }
            override fun toString(): String = "{ isLeaf=$isLeaf, children=$children }"
        }

    override fun add(element: String): Boolean {
        val new: () -> MutableTrieNode = {
            object: MutableTrieNode {
                override var isLeaf: Boolean = false
                override var children: MutableMap<Char, MutableTrieNode>? = null
                override fun toString(): String = "{ isLeaf=$isLeaf, children=$children }"
            }
        }
        var current = thisAsMutableTrieNode
        for (char in element) {
            if (current.children == null) current.children = LinkedHashMap(4)
            current = current.children!!.getOrPut(char, new)
        }
        return if (current.isLeaf.not()) {
            current.isLeaf = true
            innerSize++
            true
        } else false
    }

    override fun addAll(elements: Collection<String>): Boolean = elements.any { this@HashTrie.add(it).not() }

    override fun clear() {
        innerIsLeaf = false
        innerChildren = null // 메모리 누수 확인 안함
        innerSize = 0
    }

    override fun remove(element: String): Boolean {
        var current = thisAsMutableTrieNode
        for (char in element) current = current.children?.get(char)?:return false
        if (current.isLeaf.not()) return false
        TODO("Not yet implemented")
    }

    override fun removeAll(elements: Collection<String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements: Collection<String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "{ isLeaf=$isLeaf, children=$children }"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HashTrie

        if (isLeaf != other.isLeaf) return false
        if (children != other.children) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLeaf.hashCode()
        result = 31 * result + (children?.hashCode() ?: 0)
        result = 31 * result + size
        return result
    }
}