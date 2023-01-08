package net.loute.freem.compiler.util

fun processorOf(vararg process: () -> Boolean) = arrayOf(*process)
fun runProcess(vararg process: () -> Boolean) = process.any { it() }
fun runProcessor(processor: Array<() -> Boolean>) = processor.any { it() }