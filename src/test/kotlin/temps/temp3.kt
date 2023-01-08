package net.loute.freem.compiler.temps

import net.loute.freem.compiler.symbolTable.tryCompile

fun main() {
    tryCompile {
        throwError("test error", "C:\\Users\\phill\\WorkSpace\\Loute\\Freem Programming Language\\freem-compiler\\src\\test\\resources\\sample-code.fr", 33, 10, 0)
    }
}