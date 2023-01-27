package net.loute.freem.compiler.symbol.table.frontend.token

import net.loute.freem.compiler.util.ElementDuplicateException
import net.loute.freem.compiler.util.Trie

val sampleRule = tokens {
    interfacingBlock("skip") {

    }
    interfacingBlock("separator") {

    }
    interfacingBlock("keyword") {

    }
    interfacingBlock("operator") {

    }
}

sealed interface TokenNode
sealed interface TokenRule
data class TokenType(val name: String, val interfaces: Set<String>, val tokenRule: TokenRule): TokenNode

typealias TokenSetBuilderBlock = TokenSetBuilder.() -> Unit

abstract class TokenSetBuilder internal constructor() {
    abstract val tokenSet: TokenSet
    val interfaces = mutableSetOf<String>()
    fun token(name: String, vararg interfaces: String, tokenRule: TokenRule) = tokenSet.tokens.add(TokenType(name, interfaces.toSet(), tokenRule))
    fun group(name: String, tokenBuilder: TokenSetBuilderBlock) {}

    /**
     * define interface for token
     * @param name interface name
     * @throws ElementDuplicateException when interface is already defined
     */
    fun defineInterface(name: String) {
        if (!interfaces.add(name)) throw ElementDuplicateException("interface named \"$name\" is already defined")
    }
    fun startInterfacing(name: String) = interfaces.add(name)
    fun stopInterfacing(name: String) = interfaces.remove(name)
    inline fun interfacingBlock(name: String, tokenBuilder: TokenSetBuilderBlock) {
        startInterfacing(name)
        tokenBuilder()
        stopInterfacing(name)
    }
}

fun tokens(tokenSetBuilder: TokenSetBuilderBlock) =
    object: TokenSetBuilder() { override val tokenSet = TokenRoot(mutableListOf()) }.also(tokenSetBuilder).tokenSet
sealed interface TokenSet { val tokens: MutableCollection<TokenNode> }
data class TokenRoot(override val tokens: MutableList<TokenNode>): TokenSet
data class TokenGroup(val name: String, val tokens: MutableList<TokenNode>): TokenNode

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

//class Token private constructor(val type: Type, val lexeme: String, val range: SourceRange) {
//    constructor(type: StaticType, range: SourceRange): this(type, type.value, range)
//    constructor(type: StaticType, startRow: UInt, startColumn: UInt, endRow: UInt, endColumn: Unit): this(type, type.value, SourceRange(startColumn, endColumn))
//
//    sealed interface Type
//    sealed class StaticType(val value: String): Type {
//
//        companion object {
//            val data =
//            operator fun get(type: String) =
//        }
//    }
//    object EOF: StaticType("\u0000")
//    sealed class Separator(value: String): StaticType(value)
//    sealed class Operator(value: String): StaticType(value)
//    sealed class Keyword(value: String): StaticType(value)
//
//    sealed interface DynamicType: Type
//    object IDENTIFIER: DynamicType
//    sealed interface Literal: DynamicType
//}

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

///**
// * @property originalText content that was read from source code
// */
//sealed interface Token_ {
//    val originalText: String // lexeme...
//
//    class TokenWhiteSpaceException(message: String? = null): Exception(message)
//    class TokenNotFoundException(message: String? = null): Exception(message)
//
//    object EOF: Token { override val originalText = "\u0000" }
//
//    data class IDENTIFIER(override val originalText: String): Token
//
//    sealed interface Literal: Token {
//        data class BOOLEAN(override val originalText: String): Literal
//        data class NUMBER(override val originalText: String): Literal
//        // temporally commented
//
////        data class BYTE(override val readContent: String): Number(readContent)
////        data class SHORT(override val readContent: String): Number(readContent)
////        data class INT(override val readContent: String): Number(readContent)
////        data class LONG(override val readContent: String): Number(readContent)
////        data class FLOAT(override val readContent: String): Number(readContent)
////        data class DOUBLE(override val readContent: String): Number(readContent)
////        data class UBYTE(override val readContent: String): Number(readContent)
////        data class USHORT(override val readContent: String): Number(readContent)
////        data class UINT(override val readContent: String): Number(readContent)
////        data class ULONG(override val readContent: String): Number(readContent)
//
//        sealed interface Text: Literal
//        data class STRING (override val originalText: String): Text
//        data class CHAR   (override val originalText: String): Text
//        data class REGEX  (override val originalText: String): Text
//    }
//
//    companion object {
//        val immutableTokenTrie = Trie(
//            arrayOf(Separator, Operator, Keyword)
//                .fold(emptyMap<String, Token>()) { acc, comp -> acc + comp.data }
//                .keys
//        )
//    }
//
//    sealed class ImmutableTokenCompanion<T: Token>(createInstance: (type: String) -> T, vararg tokenContents: String) {
//        val data = tokenContents
//            .toSet()
//            .also {
//                // if type contains white space throw TokenWhiteSpaceException
//                if (it.any { i -> i.any { c -> c.isWhitespace() } }) throw TokenWhiteSpaceException("token cannot contain white space")
//            }
//            .associateWith(createInstance)
//        operator fun invoke(type: String) = getOrNull(type) ?: throw TokenNotFoundException("token type \"$type\" not found")
//        fun getOrNull(type: String) = data[type]
//    }
//
//    class Separator private constructor(override val originalText: String): Token {
//        companion object: ImmutableTokenCompanion<Separator>(::Separator, ";", "\n")
//    }
//    class Operator private constructor(override val originalText: String): Token {
//        companion object: ImmutableTokenCompanion<Operator>(::Operator,
//            "=",
//            "+",
//            "-",
//            "*",
//            "/",
//            "%",
//            "!",
//            "+=",
//            "-=",
//            "*=",
//            "/=",
//            "%=",
//
//            "==",
//            "!=",
//            "===",
//            "!==",
//
//            "++",
//            "--",
//            "**",
//            "!!",
//
//            "<",
//            ">",
//
//            "<=",
//            ">=",
//
//            "&&",
//            "||",
//
//            "(",
//            ")",
//            "{",
//            "}",
//            "[",
//            "]",
//
//            ".",
//            ",",
//            "?",
//            ":",
//            "::",
//
//            "->",
//            "=>",
//
//            "@",
//
//            "&",
//            "|",
//            "^",
//            "~",
//            "<<",
//            ">>",
//        )
//    }
//    class Keyword private constructor(override val originalText: String): Token {
//        companion object: ImmutableTokenCompanion<Keyword>(::Keyword,
//            "null",
//
//            "for",
//            "while",
//            "do",
//
//            "const",
//            "val",
//            "var",
//
//            "vararg",
//
//            "func",
//            "main",
//            "inline",
//            "infix",
//
//            "if",
//            "elif",
//            "else",
//
//            "return",
//            "break",
//            "continue",
//
//            "switch",
//            "when",
//
//            "is",
//            "by",
//
//            "data",
//            "class",
//            "object",
//            "enum",
//            "interface",
//            "abstract",
//
//            "operator",
//
//            "public",
//            "protected",
//            "default",
//            "private",
//
//            "final",
//
//            "this",
//            "super",
//
//            "package",
//            "import",
//            "as",
//
//            "open",
//            "override",
//
//            "annotation",
//        )
//    }
//}

/*
sealed interface Token {
    val lexeme: String

    companion object {
        val trie = Trie(
            arrayOf(
                *Partition.values(),
                *Operator.values(),
                *Keyword.values(),
            ).map { it.lexeme }
        )

        private inline fun checkLexeme(name: String, lexeme: String, values: List<String>) {
            if (lexeme.any { it.isWhitespace() }) throw Exception("$name cannot contain whitespace")
            val duplicates = values.findAllDuplicates()
            if (duplicates.isNotEmpty()) throw Exception("$name cannot has duplicate: ${duplicates.joinToString()}")
        }
    }

    object EOF: Token { override val lexeme = "\u0000" }

    enum class Partition(override val lexeme: String): Token {
        SEMICOLON(";"),
        LINEBREAK("\n"),
    }

    data class IDENTIFIER(override val lexeme: String): Token

    sealed interface Literal: Token {
        data class BOOLEAN(override val lexeme: String): Literal

        sealed interface Number: Literal
        data class BYTE   (override val lexeme: String): Number
        data class SHORT  (override val lexeme: String): Number
        data class INT    (override val lexeme: String): Number
        data class LONG   (override val lexeme: String): Number
        data class FLOAT  (override val lexeme: String): Number
        data class DOUBLE (override val lexeme: String): Number

        data class UBYTE  (override val lexeme: String): Number
        data class USHORT (override val lexeme: String): Number
        data class UINT   (override val lexeme: String): Number
        data class ULONG  (override val lexeme: String): Number

        sealed interface Text: Literal
        data class STRING (override val lexeme: String): Text
        data class CHAR   (override val lexeme: String): Text
        data class REGEX  (override val lexeme: String): Text
    }

    enum class Operator(override val lexeme: String): Token {
        EQUAL                                    ( "="   ),
        PLUS                                     ( "+"   ),
        MINUS                                    ( "-"   ),
        STAR                                     ( "*"   ),
        SLASH                                    ( "/"   ),
        PERCENT                                  ( "%"   ),
        NOT                                      ( "!"   ),
        PLUS_EQUAL                               ( "+="  ),
        MINUS_EQUAL                              ( "-="  ),
        STAR_EQUAL                               ( "*="  ),
        SLASH_EQUAL                              ( "/="  ),
        PERCENT_EQUAL                            ( "%="  ),

        EQUAL_EQUAL                              ( "=="  ),
        NOT_EQUAL                                ( "!="  ),
        EQUAL_EQUAL_EQUAL                        ( "===" ),
        NOT_EQUAL_EQUAL                          ( "!==" ),

        PLUS_PLUS                                ( "++"  ),
        MIUSE_MINUS                              ( "--"  ),
        STAR_STAR                                ( "**"  ),
        NOT_NOT                                  ( "!!"  ),

        LESS                                     ( "<"   ),
        GREATER                                  ( ">"   ),

        LESS_EQUAL                               ( "<="  ),
        GREATER_EQUAL                            ( ">="  ),

        AND                                      ( "&&"  ),
        OR                                       ( "||"  ),

        LEFT_PAREN                               ( "("   ),
        RIGHT_PAREN                              ( ")"   ),
        LEFT_BRACE                               ( "{"   ),
        RIGHT_BRACE                              ( "}"   ),
        LEFT_BRACKET                             ( "["   ),
        RIGHT_BRACKET                            ( "]"   ),

        DOT                                      ( "."   ),
        COMMA                                    ( ","   ),
        QUESTION_MARK                            ( "?"   ),
        COLON                                    ( ":"   ),
        D_COLON                                  ( "::"  ),

        SINGLE_ARROW                             ( "->"  ),
        DOUBLE_ARROW                             ( "=>"  ),

        AT                                       ( "@"   ),

        BIT_AND                                  ( "&"   ),
        BIT_OR                                   ( "|"   ),
        BIT_XOR                                  ( "^"   ),
        BIT_NOT                                  ( "~"   ),
        BIT_LEFT                                 ( "<<"  ),
        BIT_RIGHT                                ( ">>"  ),
        ;
        init { checkLexeme("Operator", lexeme, values().map { it.lexeme }) }
        companion object { val table = values().associateBy { it.lexeme } }
    }

    enum class Keyword(override val lexeme: String): Token {
        NULL                                     ( "null"       ),

        FOR                                      ( "for"        ),
        WHILE                                    ( "while"      ),
        DO                                       ( "do"         ),

        CONST                                    ( "const"      ),
        VAL                                      ( "val"        ),
        VAR                                      ( "var"        ),

        VARARG                                   ( "vararg"     ),

        FUNC                                     ( "func"       ),
        MAIN                                     ( "main"       ),
        INLINE                                   ( "inline"     ),
        INFIX                                    ( "infix"      ),

        IF                                       ( "if"         ),
        ELIF                                     ( "elif"       ),
        ELSE                                     ( "else"       ),

        RETURN                                   ( "return"     ),
        BREAK                                    ( "break"      ),
        CONTINUE                                 ( "continue"   ),

        SWITCH                                   ( "switch"     ),
        WHEN                                     ( "when"       ),

        IS                                       ( "is"         ),
        BY                                       ( "by"         ),

        DATA                                     ( "data"       ),
        CLASS                                    ( "class"      ),
        OBJECT                                   ( "object"     ),
        ENUM                                     ( "enum"       ),
        INTERFACE                                ( "interface"  ),
        ABSTRACT                                 ( "abstract"   ),

        OPERATOR                                 ( "operator"   ),

        PUBLIC                                   ( "public"     ),
        PROTECTED                                ( "protected"  ),
        DEFAULT                                  ( "default"    ),
        PRIVATE                                  ( "private"    ),

        FINAL                                    ( "final"      ),

        THIS                                     ( "this"       ),
        SUPER                                    ( "super"      ),

        PACKAGE                                  ( "package"    ),
        IMPORT                                   ( "import"     ),
        AS                                       ( "as"         ),

        OPEN                                     ( "open"       ),
        OVERRIDE                                 ( "override"   ),

        ANNOTATION                               ( "annotation" ),
        ;
        init { checkLexeme("Keyword", lexeme, values().map { it.lexeme }) }
        companion object { val table = values().associateBy { it.lexeme } }
    }
}
*/
