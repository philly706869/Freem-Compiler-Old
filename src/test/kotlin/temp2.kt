import net.loute.freem.compiler.util.collection.mutableTrieOf
import net.loute.freem.compiler.util.collection.trieOf

fun main() {
    val trie = trieOf("a", "abc", "bc")
    println("a" in trie)    // true
    println("ab" in trie)   // false
    println("abc" in trie)  // true
    println("abcd" in trie) // false
    println(trie.size) // 3
    println(trie)
    println(trie['a'])

    val mutableTrie = mutableTrieOf("a")
    println(mutableTrie.size) // 1
    mutableTrie.add("ab") // true
    println(mutableTrie.size) // 2
    println(mutableTrie)
}