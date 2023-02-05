package net.loute.freem.compiler

import net.loute.freem.compiler.util.StringLocation

class CompileException(message: String, val pathname: String? = null, val location: StringLocation? = null): Exception(message) {
    val errorMessage by lazy { "${pathname?:"unknown path"}${location?.run { ":$row:$column:$index" }?:""}\n$message" }
    fun printError() = println(errorMessage)
}