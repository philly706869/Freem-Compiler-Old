package net.loute.freem.compiler.util

fun processorOf(vararg block: () -> Boolean) = arrayOf(*block)
fun process(vararg block: () -> Boolean) = block.any { it() }