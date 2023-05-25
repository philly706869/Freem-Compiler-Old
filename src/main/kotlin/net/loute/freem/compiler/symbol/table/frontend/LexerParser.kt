package net.loute.freem.compiler.symbol.table.frontend
//
//interface Pattern
//
//class RawPattern(private val rule: Rule): Pattern {
//    fun compiled(): CompiledPattern = CompiledPattern(rule)
//}
//data class CompiledPattern(private val rule: Rule): Pattern
//
//abstract class Rule {
//    val plus: Plus get() = Plus(this)
//    val star: Star get() = Star(this)
//}
//
//data class Plus(val rule: Rule): Rule()
//data class Star(val rule: Rule): Rule()
//
//data class Text(val str: String): Rule() {
//
//}
//
//fun text(string: String) = Text(string)