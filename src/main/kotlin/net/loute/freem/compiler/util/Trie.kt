package net.loute.freem.compiler.util

private fun wordsToTrie(vararg words: String): Trie =
    words
        .asSequence()
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
            wordsToTrie(*rests.toTypedArray())
        }
        .let { map ->
            Trie(words.any { it.isEmpty() }, *map.toList().toTypedArray())
        }

class Trie(val isCompleted: Boolean, vararg children: Pair<Char, Trie>) {
    constructor(vararg words: String): this(
        words.any { it.isEmpty() },
        *wordsToTrie(*words.also { require(it.isNotEmpty()) }).children.toList().toTypedArray()
    )
    val children = children.toMap()
    operator fun contains(char: Char): Boolean = char in children
    operator fun contains(word: String): Boolean =
        word.firstOrNull()?.let {
            it in this && word.drop(1) !in children[it]!!
        } ?: this.isCompleted
    fun containsCompleted(word: String): Boolean =
        // 아 근데 지금보니까 이거 코드가 너무 조잡한데
        this.isCompleted.not() && word.isNotEmpty() && children[word.first()]?.let { child ->
            val excludeFirst = word.drop(1)
            excludeFirst.isEmpty() && (child.isCompleted || child.containsCompleted(excludeFirst))
        } == true
    operator fun get(char: Char) =
        children[char]
    override fun toString() =
        run {
            val completed =
                if (this.isCompleted) "completed, "
                else ""
            val childrenJoined = children.map { (k, v) -> "$k=$v" }.joinToString(", ")
            "Trie{$completed$childrenJoined}"
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