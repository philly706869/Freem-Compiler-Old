package net.loute.freem.compiler

import net.loute.freem.compiler.symbolTable.back.Assembler
import net.loute.freem.compiler.symbolTable.back.CodeGenerator
import net.loute.freem.compiler.symbolTable.back.Optimizer
import net.loute.freem.compiler.symbolTable.front.IntermediateRepresentationGenerator
import net.loute.freem.compiler.symbolTable.front.Lexer
import net.loute.freem.compiler.symbolTable.front.Parser
import net.loute.freem.compiler.symbolTable.front.SemanticAnalyzer
import net.loute.freem.compiler.util.readFile
import java.io.File

object FreemCompiler {
    class FreemCode(private val content: String) { operator fun invoke() = content }
    fun compile(freemCode: FreemCode) = Assembler(Optimizer(CodeGenerator(IntermediateRepresentationGenerator(SemanticAnalyzer(Parser(Lexer(freemCode)))))))
    fun compile(file: File) { compile(FreemCode(readFile(file))) }
    fun compile(pathName: String) { compile(File(pathName)) }
}

