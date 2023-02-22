fun main() {
    val a = mutableListOf<Int>(1, 2, 3, 4, 5, 5, 5, 4, 1, 0)
    val b = mutableListOf<Int>(2, 3, 4, 5)
    println(a.retainAll(b))
    println(a)
    println(b)
}