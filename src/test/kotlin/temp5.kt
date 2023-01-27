fun main() {
    val a = Test(1)
    val b = Test(2)
    println(a == b)
}

data class Test(val a: Int)