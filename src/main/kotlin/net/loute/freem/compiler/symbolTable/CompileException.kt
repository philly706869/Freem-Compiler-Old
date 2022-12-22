package net.loute.freem.compiler.symbolTable

import java.lang.Exception

class CompileException(message: String): Exception(message)
inline fun throwCompileError(message: String) { throw CompileException(message) }
inline fun throwCompileError(message: () -> String) { throwCompileError(message()) }