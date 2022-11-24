package net.loute.freem.compiler

import net.loute.freem.compiler.util.readFile
import java.io.File

fun main() { FreemCompiler.compile(FreemCompiler.FreemCode(readFile(File("src/test/resources/sample-code.fr")))) }