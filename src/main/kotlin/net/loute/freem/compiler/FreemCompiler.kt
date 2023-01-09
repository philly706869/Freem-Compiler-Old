package net.loute.freem.compiler

import net.loute.freem.compiler.symbolTable.CompileException
import net.loute.freem.compiler.symbolTable.backend.Assembler
import net.loute.freem.compiler.symbolTable.backend.CodeGenerator
import net.loute.freem.compiler.symbolTable.backend.Optimizer
import net.loute.freem.compiler.symbolTable.frontend.IntermediateRepresentationGenerator
import net.loute.freem.compiler.symbolTable.frontend.Lexer
import net.loute.freem.compiler.symbolTable.frontend.Parser
import net.loute.freem.compiler.symbolTable.frontend.SemanticAnalyzer
import net.loute.freem.compiler.symbolTable.frontend.token.Token
import net.loute.freem.compiler.util.pipe
import java.io.File
import java.nio.charset.Charset

object FreemCompiler {
    fun compile(file: File, charset: Charset = Charsets.UTF_8) {
        try {
            if (!file.isFile) throw CompileException("file does not exist")

            Lexer.lexicalAnalyse(file, charset).apply {
                forEach {
                    """
                    {
                        type: ${it},
                        lexeme: "${it.lexeme}",
                    },
                """.trimIndent() pipe ::println
                }
            } pipe
                    Parser::parseAnalyse pipe
                    SemanticAnalyzer::semanticAnalyse pipe
                    IntermediateRepresentationGenerator::generateIntermediateRepresentation pipe
                    CodeGenerator::generateCode pipe
                    Optimizer::optimize pipe
                    Assembler::generateMachineLanguage
        } catch (e: CompileException) { e.printError() }
    }
    fun compile(pathname: String) { compile(File(pathname)) }
}


