//enum class Keyword: IStaticTokenType {
//
//}
//
//enum class Operator: IStaticTokenType {
//
//}
//
//enum class Separator: IStaticTokenType {
//
//}
//
//sealed interface Literal: IDynamicTokenType
//enum class Number: Literal {
//
//}
//// all: ^('(\\\\.|[^\\\\])'|\"\"\"(\\\\[\\s\\S]|[^\"\\\\])*\"\"\"|\"(\\\\.|[^\"\\\\])*\"|\\d+(u|U)?(b|B|s|S|l|L|f|F)?|\\d*\\.\\d+(u|U)?(f|F)?)
//// char:
//// string:
//// string(multi):
//// regex:
////
//
//fun test() {
//
//}
//
//enum class Text(override val structure: TokenStructure): Literal {
//    String(tokenStructureOf(text("\""), text("\""))),
//    Char(),
//    Regex(),
//}
//
//object Identifier: IDynamicTokenType {
//    override val structure = tokenStructureOf(letter_, star(word))
//}
//object WhiteSpace: IDynamicTokenType {
//    override val structure = tokenStructureOf(plus(whiteSpace))
//}
//object LineBreak: IStaticTokenType { override val value = "\n" }
//
//val sourceCode =
//"""
//
//""".trimIndent()
//
//val lexer = Lexer(sourceCode) {
//    add(Keyword.values())
//    add(Operator.values())
//    add(Separator.values())
//    add(Literal.values())
//    add(Identifier, WhiteSpace)
//}
//
//fun main() {
//
//}
