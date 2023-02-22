package net.loute.freem.compiler

import net.loute.freem.compiler.symbol.table.backend.Assembler
import net.loute.freem.compiler.symbol.table.backend.CodeGenerator
import net.loute.freem.compiler.symbol.table.backend.Optimizer
import net.loute.freem.compiler.symbol.table.frontend.*
import net.loute.freem.compiler.symbol.table.frontend.lexerVersions.CLexer
import net.loute.freem.compiler.util.pipe
import java.io.File
import java.nio.charset.Charset

object FreemCompiler {
    fun compile(sourceCode: String, pathname: String? = null) {
        try {
            CLexer.lexicalAnalyse(sourceCode, pathname) pipe
            Parser::parseAnalyse pipe
            SemanticAnalyzer::semanticAnalyse pipe
            IntermediateRepresentationGenerator::generateIntermediateRepresentation pipe
            CodeGenerator::generateCode pipe
            Optimizer::optimize pipe
            Assembler::generateMachineLanguage
        } catch (e: CompileException) { e.printError() }
    }
    fun compile(file: File, charset: Charset = Charsets.UTF_8) {
        try {
            if (!file.isFile) throw CompileException("file does not exist")
            compile(file.readText(charset), file.absolutePath)
        } catch (e: CompileException) { e.printError() }
    }
    fun compile(pathname: String, charset: Charset = Charsets.UTF_8) { compile(File(pathname), charset) }
}


