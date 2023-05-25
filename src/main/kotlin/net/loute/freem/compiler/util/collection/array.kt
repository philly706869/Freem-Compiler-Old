package net.loute.freem.compiler.util.collection

fun <T> Array<T>.hasDuplicate() = size != distinct().count()

fun <T> Array<T>.findDuplicate() = run {
    val buffer = mutableSetOf<T>()
    firstOrNull { !buffer.add(it) }
}

fun <T> Array<T>.findAllDuplicates() = run {
    val buffer = mutableSetOf<T>()
    filter { !buffer.add(it) }
}

fun <T> Collection<T>.hasDuplicate() = size != distinct().count()

fun <T> Collection<T>.findDuplicate() = run {
    val buffer = mutableSetOf<T>()
    firstOrNull { !buffer.add(it) }
}

fun <T> Collection<T>.findAllDuplicates() = run {
    val buffer = mutableSetOf<T>()
    filter { !buffer.add(it) }
}