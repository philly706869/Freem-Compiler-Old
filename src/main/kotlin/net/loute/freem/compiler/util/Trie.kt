package net.loute.freem.compiler.util

fun Array<out String>.toTrie(): Trie = asSequence().toTrie()
fun Iterable<String>.toTrie(): Trie = asSequence().toTrie()
fun Sequence<String>.toTrie(): Trie =
    Trie(
        any { it.isEmpty() },
        filter { it.isNotEmpty() }
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
                rests.toTrie()
            }
    )

class Trie(val isCompleted: Boolean, val children: Map<Char, Trie>) {
    constructor(words: Array<out String>): this(
        words.any { it.isEmpty() },
        words.also { require(it.isNotEmpty()) }.asSequence().toTrie().children
    )
    constructor(vararg words: String): this(words)
    constructor(words: Sequence<String>): this(words.toList())
    constructor(words: Iterable<String>): this(words.toList().toTypedArray())
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
    operator fun get(char: Char) = children[char]
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

// 문화유산 -1호
/*
  _____   _                                      _____  _     _ _ _
 |_   _| | |                                    |  __ \| |   (_) | |
   | |   | | _____   _____   _   _  ___  _   _  | |__) | |__  _| | |_   _
   | |   | |/ _ \ \ / / _ \ | | | |/ _ \| | | | |  ___/| '_ \| | | | | | |
  _| |_  | | (_) \ V /  __/ | |_| | (_) | |_| | | |    | | | | | | | |_| |  ♥
 |_____| |_|\___/ \_/ \___|  \__, |\___/ \__,_| |_|    |_| |_|_|_|_|\__, |
                              __/ |                                  __/ |
 by AiDEN                    |___/                                  |___/
*/