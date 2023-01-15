package net.loute.freem.compiler.main

import net.loute.freem.compiler.FreemCompiler
import net.loute.freem.compiler.CompileException

fun main(args: Array<String>) {
    try {
        val iterator = args.iterator()
        if (!iterator.hasNext()) throw CompileException(MESSAGE.HELP)
        val pathname = iterator.next()

        val options = mutableMapOf<String, Array<String>>()

        while (iterator.hasNext()) {
            val option = iterator.next()
            if (option.startsWith("-")) {
                //while ()
            }
        }

        //FreemCompiler.compile(pathname)
    } catch (e: CompileException) { e.printError() }
}

