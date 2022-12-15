package net.loute.freem.compiler

import net.loute.freem.compiler.symbolTable.CompileException

fun main(args: Array<String>) = compile(args[0])

fun compile(pathName: String) {
    try {
        FreemCompiler.compile(pathName)
    } catch (e: CompileException) {
        println(e.message)
    }
}