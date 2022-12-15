package net.loute.freem.compiler.symbolTable.frontend.token

sealed interface Token {
    interface PolymorphicToken: Token { val lexeme: String }
    data class IDENTIFIER(override val lexeme: String): Token, PolymorphicToken
    data class LITERAL(override val lexeme: String): Token, PolymorphicToken
    object LINEBREAK: Token { override fun toString() = "LINEBREAK" }

    enum class Operator(val value: String, isCombineDirectionRight: Boolean = false): Token {
        EQUAL            ( value = "="                                  ),
        PLUS             ( value = "+"                                  ),
        MINUS            ( value = "-"                                  ),
        STAR             ( value = "*"                                  ),
        SLASH            ( value = "/"                                  ),
        PERCENT          ( value = "%"                                  ),
        NOT              ( value = "!"                                  ),
        PLUS_EQ          ( value = "+="                                 ),
        MINUS_EQ         ( value = "-="                                 ),
        STAR_EQ          ( value = "*="                                 ),
        SLASH_EQ         ( value = "/="                                 ),
        PERCENT_EQ       ( value = "%="                                 ),
        NOT_EQ           ( value = "!="                                 ),

        D_EQUAL          ( value = "=="                                 ),
        D_PLUS           ( value = "++"                                 ),
        D_MINUS          ( value = "--"                                 ),
        D_STAR           ( value = "**", isCombineDirectionRight = true ),
        D_NOT            ( value = "!!"                                 ),

        T_EQUAL          ( value = "==="                                ),

        LESS             ( value = "<"                                  ),
        GREATER          ( value = ">"                                  ),

        LESS_EQUAL       ( value = "<="                                 ),
        GREATER_EQUAL    ( value = ">="                                 ),

        AND              ( value = "&&"                                 ),
        OR               ( value = "||"                                 ),

        LEFT_PAREN       ( value = "("                                  ),
        RIGHT_PAREN      ( value = ")"                                  ),
        LEFT_BRACE       ( value = "{"                                  ),
        RIGHT_BRACE      ( value = "}"                                  ),
        LEFT_BRACKET     ( value = "["                                  ),
        RIGHT_BRACKET    ( value = "]"                                  ),

        DOT              ( value = "."                                  ),
        COMMA            ( value = ","                                  ),
        QUESTION_MARK    ( value = "?"                                  ),
        SEMICOLON        ( value = ";"                                  ),
        COLON            ( value = ":"                                  ),
        D_COLON          ( value = "::"                                 ),

        SINGLE_ARROW     ( value = "->"                                 ),
        DOUBLE_ARROW     ( value = "=>"                                 ),

        AT               ( value = "@"                                  ),

        BIT_AND          ( value = "&"                                  ),
        BIT_OR           ( value = "|"                                  ),
        BIT_XOR          ( value = "^"                                  ),
        BIT_NOT          ( value = "~"                                  ),
        BIT_LEFT         ( value = "<<"                                 ),
        BIT_RIGHT        ( value = ">>"                                 ),
        ;
        enum class CombineDirection { LEFT, RIGHT }
        val combineDirection = if (isCombineDirectionRight) CombineDirection.RIGHT else CombineDirection.LEFT
        companion object { val table = mapOf(*values().map { it.value to it }.toTypedArray()) }
    }

    enum class Keyword: Token {
        NULL             ,
        MAIN             ,

        FOR              ,
        WHILE            ,
        DO               ,

        CONST            ,
        VAL              ,
        VAR              ,

        VARARG           ,

        TRUE             ,
        FALSE            ,

        FUNC             ,
        INLINE           ,
        INFIX            ,

        IF               ,
        ELIF             ,
        ELSE             ,

        RETURN           ,
        BREAK            ,
        CONTINUE         ,

        SWITCH           ,
        WHEN             ,

        IS               ,
        BY               ,
        TO               ,
        SKIP             ,

        DATA             ,
        CLASS            ,
        OBJECT           ,
        ENUM             ,
        INTERFACE        ,
        ABSTRACT         ,

        OPERATOR         ,

        PUBLIC           ,
        PROTECTED        ,
        DEFAULT          ,
        PRIVATE          ,

        FINAL            ,

        THIS             ,
        SUPER            ,

        PACKAGE          ,
        IMPORT           ,
        AS               ,

        OPEN             ,
        OVERRIDE         ,

        ANNOTATION       ,

        GET              ,
        SET              ,
        ;
        companion object { val table = mapOf(*values().map { it.name.lowercase() to it }.toTypedArray()) }
    }
}
