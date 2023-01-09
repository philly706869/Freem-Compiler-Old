package net.loute.freem.compiler.util

typealias CutFn = () -> Unit
typealias Chain = (CutFn) -> Unit

/**
 * example:
 * ```
 * runChains(
 *     { cut ->
 *          if (condition) cut()
 *     },
 *     {
 *          println("ok")
 *     }
 * )
 * ```
 */
fun runChains(vararg chains: Chain) {
    var isCut = false
    fun cut() {
        isCut = true
    }
    val iterator = chains.iterator()
    if (!iterator.hasNext()) return
    var currentChain = iterator.next()
    while (!isCut) {
        currentChain(::cut)
        if (!iterator.hasNext()) break
        currentChain = iterator.next()
    }
}
