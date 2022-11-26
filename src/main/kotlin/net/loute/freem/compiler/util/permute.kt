package net.loute.freem.compiler.util

fun <T> Collection<T>.permuted(): List<List<T>> = flatMapIndexed { index, t ->
    with(filterIndexed { it, _ -> index != it }) {
        if (isEmpty()) listOf(listOf(t))
        else permuted().map { listOf(t) + it }
    }
}

fun <T> Array<T>.permuted(): List<List<T>> = flatMapIndexed { index, t ->
    with(filterIndexed { it, _ -> index != it }) {
        if (isEmpty()) listOf(listOf(t))
        else permuted().map { listOf(t) + it }
    }
}
