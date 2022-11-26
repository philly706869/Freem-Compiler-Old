package net.loute.freem.compiler.symbolTable.back

import net.loute.freem.compiler.symbolTable.front.IntermediateRepresentationGenerator

object CodeGenerator {
    class AssemblyLanguage
    operator fun invoke(intermediateRepresentation: IntermediateRepresentationGenerator.IntermediateRepresentation) = AssemblyLanguage().apply {

    }
}