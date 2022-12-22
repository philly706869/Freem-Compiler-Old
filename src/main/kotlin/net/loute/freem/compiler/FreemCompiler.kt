package net.loute.freem.compiler

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
    fun compileCode(code: String) =
        (code pipe Lexer::lexicalAnalyse).apply {
            forEach {
                println(
                    when (it) {
                        is Token.PolymorphicToken -> {
                            """
                        {
                            type: ${it},
                            lexeme: "${it.lexeme}",
                        },
                        """.trimIndent()
                        }
                        else -> {
                            """
                        { type: $it },
                        """.trimIndent()
                        }
                    }

                )
            }
        } pipe
                Parser::parseAnalyse pipe
                SemanticAnalyzer::semanticAnalyse pipe
                IntermediateRepresentationGenerator::generateIntermediateRepresentation pipe
                CodeGenerator::generateCode pipe
                Optimizer::optimize pipe
                Assembler::generateMachineLanguage
    fun compile(file: File, charset: Charset = Charsets.UTF_8) = compileCode(file.readText(charset))
    fun compile(path: String) = compile(File(path))
}


