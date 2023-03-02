package net.loute.freem.compiler.symbol.table.frontend.token

import net.loute.freem.compiler.util.collection.trieOf
import net.loute.freem.compiler.util.range.StringRange

sealed interface Token {
    val type: TokenType
    sealed interface InlineToken: Token {
        override val type: TokenType.InlineTokenType
        val lexeme: String
        val range: StringRange
    }

    data class Abstract(override val type: TokenType.Abstract): Token
    data class Static(
        override val type: TokenType.Static,
        override val range: StringRange,
    ): InlineToken {
        override val lexeme get() = type.staticValue
    }
    data class Dynamic(
        override val type: TokenType.Dynamic,
        override val lexeme: String,
        override val range: StringRange,
    ): InlineToken

    companion object {
        operator fun invoke(type: TokenType.Abstract) = Abstract(type)
        operator fun invoke(type: TokenType.Static, range: StringRange) = Static(type, range)
        operator fun invoke(type: TokenType.Dynamic, lexeme: String, range: StringRange) = Dynamic(type, lexeme, range)
    }
}

sealed interface TokenType {
    sealed interface InlineTokenType: TokenType
    interface Static: InlineTokenType { val staticValue: String }
    interface Dynamic: InlineTokenType
    interface Abstract: TokenType
}

//data class TokenTypeSet(set: Set<TokenTypeSet>) {
//
//}

sealed interface TokenTypes {
    object EOF: TokenType.Abstract, TokenTypes
    object IDENTIFIER: TokenType.Dynamic, TokenTypes

    sealed interface Literal: TokenType.Dynamic, TokenTypes {
        sealed interface Number: Literal
        object BYTE   : Number
        object SHORT  : Number
        object INT    : Number
        object LONG   : Number
        object FLOAT  : Number
        object DOUBLE : Number

        object UBYTE  : Number
        object USHORT : Number
        object UINT   : Number
        object ULONG  : Number

        sealed interface Text: Literal
        object STRING_CONTENT       : Text
        object STRING_INTERPOLATION : Text
        object CHAR                 : Text
        object REGEX                : Text
    }

    enum class Separator(override val staticValue: String): TokenType.Static, TokenTypes {
        SEMICOLON(";"  ),
        LINEBREAK("\n" ),
        ;
        companion object {
            val table = Operator.values().associateWith { it.staticValue }
            val trie = trieOf(*Operator.values().map { it.staticValue }.toTypedArray())
        }
    }

    enum class Operator(override val staticValue: String): TokenType.Static, TokenTypes {
        EQUAL             ( "="   ),
        PLUS              ( "+"   ),
        MINUS             ( "-"   ),
        STAR              ( "*"   ),
        SLASH             ( "/"   ),
        PERCENT           ( "%"   ),
        NOT               ( "!"   ),
        PLUS_EQUAL        ( "+="  ),
        MINUS_EQUAL       ( "-="  ),
        STAR_EQUAL        ( "*="  ),
        SLASH_EQUAL       ( "/="  ),
        PERCENT_EQUAL     ( "%="  ),

        EQUAL_EQUAL       ( "=="  ),
        NOT_EQUAL         ( "!="  ),
        EQUAL_EQUAL_EQUAL ( "===" ),
        NOT_EQUAL_EQUAL   ( "!==" ),

        PLUS_PLUS         ( "++"  ),
        MINUS_MINUS       ( "--"  ),
        STAR_STAR         ( "**"  ),
        NOT_NOT           ( "!!"  ),

        LESS              ( "<"   ),
        GREATER           ( ">"   ),

        LESS_EQUAL        ( "<="  ),
        GREATER_EQUAL     ( ">="  ),

        AND               ( "&&"  ),
        OR                ( "||"  ),

        LEFT_PAREN        ( "("   ),
        RIGHT_PAREN       ( ")"   ),
        LEFT_BRACE        ( "{"   ),
        RIGHT_BRACE       ( "}"   ),
        LEFT_BRACKET      ( "["   ),
        RIGHT_BRACKET     ( "]"   ),

        DOT               ( "."   ),
        COMMA             ( ","   ),
        QUESTION_MARK     ( "?"   ),
        COLON             ( ":"   ),
        D_COLON           ( "::"  ),

        DOUBLE_QUOTATION  ( "\""  ),

        SINGLE_ARROW      ( "->"  ),
        DOUBLE_ARROW      ( "=>"  ),

        AT                ( "@"   ),

        BIT_AND           ( "&"   ),
        BIT_OR            ( "|"   ),
        BIT_XOR           ( "^"   ),
        BIT_NOT           ( "~"   ),
        BIT_LEFT          ( "<<"  ),
        BIT_RIGHT         ( ">>"  ),
        ;
        companion object {
            val table = values().associateWith { it.staticValue }
            val trie = trieOf(*values().map { it.staticValue }.toTypedArray())
        }
    }

    enum class Keyword(override val staticValue: String): TokenType.Static, TokenTypes {
        NULL       ( "null"       ),

        TRUE       ( "true"       ),
        FALSE      ( "false"      ),

        FOR        ( "for"        ),
        WHILE      ( "while"      ),
        DO         ( "do"         ),

        CONST      ( "const"      ),
        VAL        ( "val"        ),
        VAR        ( "var"        ),

        VARARG     ( "vararg"     ),

        FUNC       ( "func"       ),
        MAIN       ( "main"       ),
        INLINE     ( "inline"     ),
        INFIX      ( "infix"      ),

        IF         ( "if"         ),
        ELIF       ( "elif"       ),
        ELSE       ( "else"       ),

        RETURN     ( "return"     ),
        BREAK      ( "break"      ),
        CONTINUE   ( "continue"   ),

        SWITCH     ( "switch"     ),
        WHEN       ( "when"       ),

        IS         ( "is"         ),
        BY         ( "by"         ),

        DATA       ( "data"       ),
        CLASS      ( "class"      ),
        OBJECT     ( "object"     ),
        ENUM       ( "enum"       ),
        INTERFACE  ( "interface"  ),
        ABSTRACT   ( "abstract"   ),

        OPERATOR   ( "operator"   ),

        PUBLIC     ( "public"     ),
        PROTECTED  ( "protected"  ),
        DEFAULT    ( "default"    ),
        PRIVATE    ( "private"    ),

        FINAL      ( "final"      ),

        THIS       ( "this"       ),
        SUPER      ( "super"      ),

        PACKAGE    ( "package"    ),
        IMPORT     ( "import"     ),
        AS         ( "as"         ),

        OPEN       ( "open"       ),
        OVERRIDE   ( "override"   ),

        ANNOTATION ( "annotation" ),
        ;
        companion object { val table = values().associateBy { it.staticValue } }
    }
}

val operatorMap = mapOf<String, TokenTypes.Operator>(

)

//val tokenStrict =

//fun main() {
//    tokenStruct {
//        static(
//            "val",
//            "var",
//        )
//        dynamic("identifier", rule())
//    }
//}
//
//class ElementDuplicateException(message: String? = null): Exception(message)
//
//typealias TokenStructBuilderBlock = TokenStructBuilder.() -> Unit
//
//class TokenStructBuilder {
//    companion object {
//        operator fun invoke(builder: TokenStructBuilderBlock) = TokenStructBuilder(builder).build()
//    }
//    constructor()
//    constructor(builder: TokenStructBuilderBlock) { this.builder() }
//    fun build() = TokenStruct()
//
//    private val tokens = mutableSetOf<TokenType>()
//
//    fun static(vararg name: String) {
//        // TODO find duplicate and throw Exception code
//        name.map { StaticTokenType(it) }
//    }
//    fun dynamic(name: String, rule: RootRule) {
//
//    }
//}
//
//data class TokenStruct(val struct: Collection<>)
//sealed interface TokenType { val name: String }
//data class StaticTokenType(val name: String)
//data class DynamicTokenType(val name: String, val rule: RootRule)
//
//fun rule(vararg rule: Rule<*>) = RootRule(rule.toList())
//
//sealed interface Rule<T> { val data: T }
//data class RootRule(override val data: Collection<Rule<*>>): Rule<Collection<Rule<*>>>
//data class Expect(override val data: String): Rule<String>
//
//fun tokenStruct(builder: TokenStructBuilderBlock) = TokenStructBuilder()

//val sampleRule = tokens {
//    ib("skip") {
//        token(";")
//    }
//    ib("separator") {
//
//    }
//    ib("keyword") {
//
//    }
//    ib("operator") {
//
//    }
//}
//
//sealed interface TokenNode
//data class TokenType(val name: String, val interfaces: Set<String>, val tokenRule: TokenRule): TokenNode
//
//sealed interface TokenRule <T> { val data: T }
//data class Text(override val data: String): TokenRule<String>
//data class Or(override val data: Collection<TokenRule<*>>): TokenRule<Collection<TokenRule<*>>>
//// at least
//// regex is definitely not good solution
//private const val lowerAlphabet = "abcdefghijklmnopqrstuvwxyz"
//private const val upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//private const val alphabet = lowerAlphabet + upperAlphabet
//
//object Alphabet: TokenRule<Unit> { override val data = Unit }
//object LowerAlphabet: TokenRule<>
//
//
//typealias TokenSetBuilderBlock = TokenSetBuilder.() -> Unit
//
//data class Interface(val name: String)
//
//abstract class TokenSetBuilder internal constructor() {
//    abstract val tokenSet: TokenSet
//    private val interfaces = mutableMapOf<String, Interface>()
//    private val interfaceBuffer = mutableSetOf<Interface>()
//    fun token(name: String, vararg interfaces: Interface, tokenRule: TokenRule<*>? = null) =
//        tokenSet.tokens.add(TokenType(name, interfaces.map { it.name }.toSet(), tokenRule?:Text(name)))
//    fun group(name: String, tokenSetBuilderBlock: TokenSetBuilderBlock) {
//        object: TokenSetBuilder() { override val tokenSet = TokenGroup(name, mutableListOf()) }.also(tokenSetBuilderBlock).tokenSet
//    }
//
//    class InterfaceDuplicateException(message: String): Exception(message)
//    class InterfaceNotFoundException(message: String): Exception(message)
//    class InterfaceNotStartedException(message: String): Exception(message)
//    class InterfaceAlreadyStartedException(message: String): Exception(message)
////
//    /**
//     * define interface
//     * @throws InterfaceDuplicateException when interface is already defined
//     */
//    fun di(name: String) {
//        if (name in interfaces.keys) throw InterfaceDuplicateException("interface named \"$name\" is already defined")
//        interfaces[name] = Interface(name)
//    }
//
//    /**
//     * start interface
//     * @throws InterfaceNotFoundException when interface is not found
//     * @throws InterfaceAlreadyStartedException when interface is already started
//     */
//    fun si(name: String) = interfaces.add(name)
//
//    /**
//     * end interface
//     * @throws InterfaceNotFoundException when interface is not found
//     * @throws InterfaceNotStartedException when interface not started
//     */
//    fun ei(name: String) = interfaces.remove(name)
//
//    /**
//     * interface block
//     * @throws InterfaceDuplicateException when interface already defined
//     */
//    inline fun ib(name: String, tokenSetBuilderBlock: TokenSetBuilderBlock) {
//        di(name)
//        si(name)
//        tokenSetBuilderBlock()
//        ei(name)
//    }
//}
//
//fun tokens(tokenSetBuilderBlock: TokenSetBuilderBlock) =
//    object: TokenSetBuilder() { override val tokenSet = TokenRoot(mutableListOf()) }.also(tokenSetBuilderBlock).tokenSet
//sealed interface TokenSet { val tokens: MutableCollection<TokenNode> }
//data class TokenRoot(override val tokens: MutableList<TokenNode>): TokenSet
//data class TokenGroup(val name: String, override val tokens: MutableList<TokenNode>): TokenSet, TokenNode

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
