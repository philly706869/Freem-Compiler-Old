package net.loute.freem.compiler.symbolTable

import net.loute.freem.compiler.util.ConsoleColor

object TryCompileBlock {
    inline fun throwError(message: String, pathname: String, line: Int, column: Int, index: Int) { throw CompileException(message, pathname, line, column, index) }
}inline fun tryCompile(block: TryCompileBlock.() -> Unit) { try { TryCompileBlock.block() } catch (e: CompileException) { e.printError() } }

class CompileException(message: String, pathname: String, val line: Int, val column: Int, val index: Int): java.lang.Exception(message) {
    val file = java.io.File(pathname)
    fun printError() {
        println("${ConsoleColor.RESET}${ConsoleColor.FONT_RED}${file.absoluteFile}:$line:$column:$index${ConsoleColor.RESET}")
        println("${ConsoleColor.RESET}${ConsoleColor.FONT_RED}$message${ConsoleColor.RESET}")
    }
}inline fun throwCompileError(message: String, pathname: String, line: Int, column: Int, index: Int) { throw CompileException(message, pathname, line, column, index) }