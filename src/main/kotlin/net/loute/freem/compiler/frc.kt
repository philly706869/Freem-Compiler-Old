package net.loute.freem.compiler

fun main(args: Array<String>) {
    try {
        FreemCompiler.compile(args[0])
    } catch (e: IndexOutOfBoundsException) {
        println(
            """
                frc <filepath> <options>
            """.trimIndent()
        )
    }
}