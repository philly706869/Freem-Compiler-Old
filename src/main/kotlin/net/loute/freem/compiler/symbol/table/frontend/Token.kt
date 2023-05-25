package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.util.location.StringLocation

data class Token(val type: TokenType, val lexeme: String, val pos: StringLocation)

enum class TokenType(val lexeme: String?) {
    EOF                   ( null         ),
    IDENTIFIER            ( null         ),

    BYTE                  ( null         ),
    SHORT                 ( null         ),
    INT                   ( null         ),
    LONG                  ( null         ),
    FLOAT                 ( null         ),
    DOUBLE                ( null         ),

    UBYTE                 ( null         ),
    USHORT                ( null         ),
    UINT                  ( null         ),
    ULONG                 ( null         ),

    STRING                ( null         ),

    CHAR                  ( null         ),
    REGEX                 ( null         ),
    REGEX_FLAG            ( null         ),

    SEMICOLON             ( ";"          ),
    LINEBREAK             ( "\n"         ),

    EQUAL                 ( "="          ),
    PLUS                  ( "+"          ),
    MINUS                 ( "-"          ),
    STAR                  ( "*"          ),
    SLASH                 ( "/"          ),
    PERCENT               ( "%"          ),
    NOT                   ( "!"          ),
    PLUS_EQUAL            ( "+="         ),
    MINUS_EQUAL           ( "-="         ),
    STAR_EQUAL            ( "*="         ),
    SLASH_EQUAL           ( "/="         ),
    PERCENT_EQUAL         ( "%="         ),

    EQUAL_EQUAL           ( "=="         ),
    NOT_EQUAL             ( "!="         ),
    EQUAL_EQUAL_EQUAL     ( "==="        ),
    NOT_EQUAL_EQUAL       ( "!=="        ),

    PLUS_PLUS             ( "++"         ),
    MIUSE_MINUS           ( "--"         ),
    STAR_STAR             ( "**"         ),
    NOT_NOT               ( "!!"         ),

    LESS                  ( "<"          ),
    GREATER               ( ">"          ),

    LESS_EQUAL            ( "<="         ),
    GREATER_EQUAL         ( ">="         ),

    AND                   ( "&&"         ),
    OR                    ( "||"         ),

    LEFT_PAREN            ( "("          ),
    RIGHT_PAREN           ( ")"          ),
    LEFT_BRACE            ( "{"          ),
    RIGHT_BRACE           ( "}"          ),
    LEFT_BRACKET          ( "["          ),
    RIGHT_BRACKET         ( "]"          ),

    DOT                   ( "."          ),
    COMMA                 ( ","          ),
    QUESTION_MARK         ( "?"          ),
    COLON                 ( ":"          ),
    D_COLON               ( "::"         ),

    SINGLE_QUOTE          ( "'"          ),
    DOUBLE_QUOTE          ( "\""         ),

    SINGLE_ARROW          ( "->"         ),
    DOUBLE_ARROW          ( "=>"         ),

    AT                    ( "@"          ),

    BIT_AND               ( "&"          ),
    BIT_OR                ( "|"          ),
    BIT_XOR               ( "^"          ),
    BIT_NOT               ( "~"          ),
    BIT_LEFT              ( "<<"         ),
    BIT_RIGHT             ( ">>"         ),

    NULL                  ( "null"       ),

    FOR                   ( "for"        ),
    WHILE                 ( "while"      ),
    DO                    ( "do"         ),

    CONST                 ( "const"      ),
    VAL                   ( "val"        ),
    VAR                   ( "var"        ),

    VARARG                ( "vararg"     ),

    FUNC                  ( "func"       ),
    MAIN                  ( "main"       ),
    INLINE                ( "inline"     ),
    INFIX                 ( "infix"      ),

    IF                    ( "if"         ),
    ELIF                  ( "elif"       ),
    ELSE                  ( "else"       ),

    RETURN                ( "return"     ),
    BREAK                 ( "break"      ),
    CONTINUE              ( "continue"   ),

    SWITCH                ( "switch"     ),
    WHEN                  ( "when"       ),

    IS                    ( "is"         ),
    BY                    ( "by"         ),

    DATA                  ( "data"       ),
    CLASS                 ( "class"      ),
    OBJECT                ( "object"     ),
    ENUM                  ( "enum"       ),
    INTERFACE             ( "interface"  ),
    ABSTRACT              ( "abstract"   ),

    OPERATOR              ( "operator"   ),

    PUBLIC                ( "public"     ),
    PROTECTED             ( "protected"  ),
    DEFAULT               ( "default"    ),
    PRIVATE               ( "private"    ),

    FINAL                 ( "final"      ),

    THIS                  ( "this"       ),
    SUPER                 ( "super"      ),

    PACKAGE               ( "package"    ),
    IMPORT                ( "import"     ),
    AS                    ( "as"         ),

    OPEN                  ( "open"       ),
    OVERRIDE              ( "override"   ),

    ANNOTATION            ( "annotation" ),
    ;
    companion object {
        val map = TokenType.values().filter { it.lexeme != null }.associateBy { it.lexeme!! }
    }
}
