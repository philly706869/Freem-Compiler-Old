package net.loute.freem.compiler

import net.loute.freem.compiler.util.ConsoleColor

class CompileException private constructor(message: String, val pathname: String?, val line: Int?, val column: Int?, val index: Int?, position: String?)
    : java.lang.Exception(message) {
    val errorMessage: String = "${ConsoleColor.RESET}${ConsoleColor.FONT_RED}${position?.run { "$position\n" }?:""}Freem: $message${ConsoleColor.RESET}"

    constructor(message: String, pathname: String? = null): this(message, pathname, null, null ,null, pathname)
    constructor(message: String, pathname: String?, line: Int, column: Int, index: Int)
            : this(message, pathname, line, column, index, "${pathname?.run { "$pathname:" }?:""}$line:$column:$index")
    constructor(message: String, line: Int, column: Int, index: Int): this(message, null, line, column, index, "$line:$column:$index")

    inline fun printError() { println(errorMessage) }
}