package net.loute.freem.compiler.symbol.table.frontend.token

interface Token {
    val lexeme: String
}

sealed interface FToken: Token {
    data class IDENTIFIER               (override val lexeme: String): FToken
    data class UNKNOWN                  (override val lexeme: String): FToken

    sealed interface Literal: FToken {
        sealed interface Number : Literal
        data class BYTE                 (override val lexeme: String) : Number
        data class SHORT                (override val lexeme: String) : Number
        data class INT                  (override val lexeme: String) : Number
        data class LONG                 (override val lexeme: String) : Number
        data class FLOAT                (override val lexeme: String) : Number
        data class DOUBLE               (override val lexeme: String) : Number

        data class UBYTE                (override val lexeme: String) : Number
        data class USHORT               (override val lexeme: String) : Number
        data class UINT                 (override val lexeme: String) : Number
        data class ULONG                (override val lexeme: String) : Number

        sealed interface Text : Literal
        data class STRING_CONTENT       (override val lexeme: String) : Text
        data class STRING_INTERPOLATION (override val lexeme: String) : Text
        data class CHAR                 (override val lexeme: String) : Text
        data class REGEX                (override val lexeme: String) : Text
    }

    enum class Separator(override val lexeme: String): FToken {
        SEMICOLON         (";"  ),
        LINEBREAK         ("\n" ),
    }

    enum class Operator(override val lexeme: String): FToken {
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
        MIUSE_MINUS       ( "--"  ),
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

        SINGLE_ARROW      ( "->"  ),
        DOUBLE_ARROW      ( "=>"  ),

        AT                ( "@"   ),

        BIT_AND           ( "&"   ),
        BIT_OR            ( "|"   ),
        BIT_XOR           ( "^"   ),
        BIT_NOT           ( "~"   ),
        BIT_LEFT          ( "<<"  ),
        BIT_RIGHT         ( ">>"  ),
    }

    enum class Keyword(override val lexeme: String): FToken {
        NULL       ( "null"       ),

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
    }
}

//sealed interface TToken {
//    val type: TokenType
//    val lexeme: String
//    val range: StringRange
//
//    data class Static(
//        override val type: TokenType.Static,
//        override val range: StringRange,
//    ): TToken { override val lexeme get() = type.staticValue }
//    data class Dynamic(
//        override val type: TokenType.Dynamic,
//        override val lexeme: String,
//        override val range: StringRange,
//    ): TToken
//
//    companion object {
//        operator fun invoke(type: TokenType.Static, range: StringRange) = Static(type, range)
//        operator fun invoke(type: TokenType.Dynamic, lexeme: String, range: StringRange) = Dynamic(type, lexeme, range)
//    }
//}
//
//sealed interface TokenType {
//    interface Static: TokenType { val staticValue: String }
//    interface Dynamic: TokenType
//}
