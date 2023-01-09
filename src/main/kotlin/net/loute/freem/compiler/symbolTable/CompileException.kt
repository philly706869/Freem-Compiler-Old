package net.loute.freem.compiler.symbolTable

import net.loute.freem.compiler.util.ConsoleColor

class CompileException
private constructor(message: String, val pathname: String?, val line: Int?, val column: Int?, val index: Int?, private val position: String?)
    : java.lang.Exception(message) {

    constructor(message: String): this(message, null, null, null, null, null)
    constructor(message: String, pathname: String): this(message, pathname, null, null ,null, pathname)
    constructor(message: String, pathname: String, line: Int, column: Int, index: Int): this(message, pathname, line, column, index, "$pathname:$line:$column:$index")

    fun printError() { println("${ConsoleColor.RESET}${ConsoleColor.FONT_RED}${position?:""}\nFreem: $message${ConsoleColor.RESET}") }
}