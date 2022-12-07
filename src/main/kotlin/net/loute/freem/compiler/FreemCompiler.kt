package net.loute.freem.compiler

import net.loute.freem.compiler.symbolTable.back.Assembler
import net.loute.freem.compiler.symbolTable.back.CodeGenerator
import net.loute.freem.compiler.symbolTable.back.Optimizer
import net.loute.freem.compiler.symbolTable.front.IntermediateRepresentationGenerator
import net.loute.freem.compiler.symbolTable.front.Lexer
import net.loute.freem.compiler.symbolTable.front.Parser
import net.loute.freem.compiler.symbolTable.front.SemanticAnalyzer
import net.loute.freem.compiler.symbolTable.front.token.Token
import net.loute.freem.compiler.util.pipe
import java.io.File
import java.nio.charset.Charset

object FreemCompiler {
    class TokenArray: ArrayList<Token>()

    fun compile(code: String) =
        code.pipe(Lexer::lexicalAnalyse).onEach {
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
            .pipe(Parser::parseAnalyse)
            .pipe(SemanticAnalyzer::semanticAnalyse)
            .pipe(IntermediateRepresentationGenerator::generateIntermediateRepresentation)
            .pipe(CodeGenerator::generateCode)
            .pipe(Optimizer::optimize)
            .pipe(Assembler::generateMachineLanguage)
    fun compile(file: File, charset: Charset = Charsets.UTF_8) { compile(file.readText(charset)) }
}

