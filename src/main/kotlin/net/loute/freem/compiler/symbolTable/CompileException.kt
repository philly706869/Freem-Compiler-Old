package net.loute.freem.compiler.symbolTable

import java.lang.Exception

class CompileException(message: String): Exception(message)
fun throwCompileError(message: String) { throw CompileException(message) }
fun throwCompileError(message: () -> String) { throwCompileError(message()) }