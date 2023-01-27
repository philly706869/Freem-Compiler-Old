import net.loute.freem.compiler.util.Trie

/* :lazy: */
val t = Trie(
    "a",
    "abc",
    "bc",
    "ca",
)

val t2 = Trie(
    "ab",
    "bcd"
)

fun main() {
    println(t + t2)
}
