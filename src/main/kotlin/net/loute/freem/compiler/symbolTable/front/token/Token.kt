package net.loute.freem.compiler.symbolTable.front.token

class Token(val type: TokenType, val lexeme: String) {
    interface TokenType
    enum class Type: TokenType {
        IDENTIFIER,
        LITERAL,
        LINEBREAK,
        ;
        enum class Operator(val value: String): TokenType {
            EQUAL            ( value = "="  ),
            PLUS             ( value = "+"  ),
            MINUS            ( value = "-"  ),
            STAR             ( value = "*"  ),
            SLASH            ( value = "/"  ),
            PERCENT          ( value = "%"  ),
            NOT              ( value = "!"  ),
            PLUS_EQ          ( value = "+=" ),
            MINUS_EQ         ( value = "-=" ),
            STAR_EQ          ( value = "*=" ),
            SLASH_EQ         ( value = "/=" ),
            PERCENT_EQ       ( value = "%=" ),
            NOT_EQ           ( value = "!=" ),

            D_EQUAL          ( value = "==" ),
            D_PLUS           ( value = "++" ),
            D_MINUS          ( value = "--" ),
            D_STAR           ( value = "**" ),
            D_NOT            ( value = "!!" ),

            LESS             ( value = "<"  ),
            GREATER          ( value = ">"  ),

            LESS_EQUAL       ( value = "<=" ),
            GREATER_EQUAL    ( value = ">=" ),

            AND              ( value = "&&" ),
            OR               ( value = "||" ),

            LEFT_PAREN       ( value = "("  ),
            RIGHT_PAREN      ( value = ")"  ),
            LEFT_BRACE       ( value = "{"  ),
            RIGHT_BRACE      ( value = "}"  ),
            LEFT_BRACKET     ( value = "["  ),
            RIGHT_BRACKET    ( value = "]"  ),

            DOT              ( value = "."  ),
            COMMA            ( value = ","  ),
            QUESTION_MARK    ( value = "?"  ),
            SEMICOLON        ( value = ";"  ),
            COLON            ( value = ":"  ),
            D_COLON          ( value = "::" ),

            SINGLE_ARROW     ( value = "->" ),
            DOUBLE_ARROW     ( value = "=>" ),

            AT               ( value = "@"  ),

            BIT_AND          ( value = "&"  ),
            BIT_OR           ( value = "|"  ),
            BIT_XOR          ( value = "^"  ),
            BIT_NOT          ( value = "~"  ),
            BIT_LEFT         ( value = "<<" ),
            BIT_RIGHT        ( value = ">>" ),
            ;
            companion object { val table = mapOf(*values().map { it.value to it }.toTypedArray()) }
        }

        enum class Keyword: TokenType {
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

            FUNCTION         ,
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
            operator fun invoke() = table
            companion object {
                val table = mapOf(*values().map { it.name.lowercase() to it }.toTypedArray())
            }
        }
    }
}