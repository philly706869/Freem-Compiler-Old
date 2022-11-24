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
    class FreemCode(val content: String)
    fun compile(freemCode: FreemCode) =
        Assembler.generateMachineLanguage(
            Optimizer.optimize(
                CodeGenerator.generateCode(
                    IntermediateRepresentationGenerator.generateIntermediateRepresentation(
                        SemanticAnalyzer.semanticAnalyse(
                            Parser.parseAnalyse(
                                Lexer.lexicalAnalyse(
                                    freemCode
                                ).onEach {
                                    println(
                                        """
                                        {
                                            type: ${it.type},
                                            lexeme: "${it.lexeme}",
                                        },
                                        """.trimIndent()
                                    )
                                }
                            )
                        )
                    )
                )
            )
        )
    fun compile(file: File) { compile(FreemCode(readFile(file))) }
    fun compile(pathName: String) { compile(File(pathName)) }
}

