package net.loute.freem.compiler.util

fun <T> Array<out T>.permuted(): List<List<T>> {
    val list = this
    return flatMapIndexed { index, x ->
            list.filterIndexed { innerIndex, _ -> index != innerIndex }
                .let { excludeThis ->
                    if (excludeThis.isEmpty()) listOf(listOf(x))
                    else excludeThis.permuted().map { listOf(x) + it }
                }
        }

}

fun <T> Iterable<T>.permuted(): List<List<T>> {
    val list = this
    return list
        .flatMapIndexed { index, x ->
            list.filterIndexed { innerIndex, _ -> index != innerIndex }
                .let { excludeThis ->
                    if (excludeThis.isEmpty()) listOf(listOf(x))
                    else excludeThis.permuted().map { listOf(x) + it }
                }
        }

}

fun <T> Sequence<T>.permuted(): List<List<T>> {
    val list = this.toList()
    return list
        .flatMapIndexed { index, x ->
            list.filterIndexed { innerIndex, _ -> index != innerIndex }
                .let { excludeThis ->
                    if (excludeThis.isEmpty()) listOf(listOf(x))
                    else excludeThis.permuted().map { listOf(x) + it }
                }
        }

}