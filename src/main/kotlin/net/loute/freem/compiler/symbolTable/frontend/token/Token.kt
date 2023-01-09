package net.loute.freem.compiler.symbolTable.frontend.token

sealed interface Token {
    val lexeme: String
    
    object LINEBREAK: Token { override val lexeme = "\n" }

    data class IDENTIFIER(override val lexeme: String): Token
    sealed interface Literal: Token {
        sealed interface Number<T : Comparable<T>>: Literal {
            fun toNumber(): T

            /**
             * This interface is for companion objects that implement the Number interface.
             * */
            sealed class NumberCompanion<T>(val suffix: String, private val instanceCreator: (lexeme: String) -> T) {
                val suffixRegex = suffix.let { "$it\$".toRegex(RegexOption.IGNORE_CASE) }
                fun new(lexeme: String) = instanceCreator(lexeme)
            }

            data class BYTE (override val lexeme: String): Number<Byte> {
                companion object: NumberCompanion<BYTE>("b", ::BYTE)
                override fun toNumber() = lexeme.replace(suffixRegex, "").toByte()
            }
            data class SHORT (override val lexeme: String) : Number<Short> {
                companion object: NumberCompanion<SHORT>("s", ::SHORT)
                override fun toNumber() = lexeme.toShort()
            }
            data class INT (override val lexeme: String) : Number<Int> {
                companion object: NumberCompanion<INT>("", ::INT)
                override fun toNumber() = lexeme.toInt()
            }
            data class LONG (override val lexeme: String) : Number<Long> {
                companion object: NumberCompanion<LONG>("l", ::LONG)
                override fun toNumber() = lexeme.toLong()
            }
            data class FLOAT (override val lexeme: String) : Number<Float> {
                companion object: NumberCompanion<FLOAT>("f", ::FLOAT)
                override fun toNumber() = lexeme.toFloat()
            }
            data class DOUBLE (override val lexeme: String) : Number<Double> {
                companion object: NumberCompanion<DOUBLE>("", ::DOUBLE)
                override fun toNumber() = lexeme.toDouble()
            }

            data class UBYTE  (override val lexeme: String) : Number<UByte> {
                companion object: NumberCompanion<UBYTE>("ub", ::UBYTE)
                override fun toNumber() = lexeme.toUByte()
            }
            data class USHORT (override val lexeme: String) : Number<UShort> {
                companion object: NumberCompanion<USHORT>("us", ::USHORT)
                override fun toNumber() = lexeme.toUShort()
            }
            data class UINT   (override val lexeme: String) : Number<UInt> {
                companion object: NumberCompanion<UINT>("u", ::UINT)
                override fun toNumber() = lexeme.toUInt()
            }
            data class ULONG  (override val lexeme: String) : Number<ULong> {
                companion object: NumberCompanion<ULONG>("ul", ::ULONG)
                override fun toNumber() = lexeme.toULong()
            }
        }

        sealed interface Text: Literal {
            data class STRING (override val lexeme: String) : Text
            data class CHAR   (override val lexeme: String) : Text
            data class REGEX  (override val lexeme: String) : Text
        }
    }

    enum class Operator(override val lexeme: String, val combineDirection: CombineDirection) : Token {
        EQUAL          ( lexeme = "="  , combineDirection = CombineDirection.LEFT  ),
        PLUS           ( lexeme = "+"  , combineDirection = CombineDirection.LEFT  ),
        MINUS          ( lexeme = "-"  , combineDirection = CombineDirection.LEFT  ),
        STAR           ( lexeme = "*"  , combineDirection = CombineDirection.LEFT  ),
        SLASH          ( lexeme = "/"  , combineDirection = CombineDirection.LEFT  ),
        PERCENT        ( lexeme = "%"  , combineDirection = CombineDirection.LEFT  ),
        NOT            ( lexeme = "!"  , combineDirection = CombineDirection.LEFT  ),
        PLUS_EQ        ( lexeme = "+=" , combineDirection = CombineDirection.LEFT  ),
        MINUS_EQ       ( lexeme = "-=" , combineDirection = CombineDirection.LEFT  ),
        STAR_EQ        ( lexeme = "*=" , combineDirection = CombineDirection.LEFT  ),
        SLASH_EQ       ( lexeme = "/=" , combineDirection = CombineDirection.LEFT  ),
        PERCENT_EQ     ( lexeme = "%=" , combineDirection = CombineDirection.LEFT  ),

        D_EQUAL        ( lexeme = "==" , combineDirection = CombineDirection.LEFT  ),
        NOT_EQ         ( lexeme = "!=" , combineDirection = CombineDirection.LEFT  ),
        T_EQUAL        ( lexeme = "===", combineDirection = CombineDirection.LEFT  ),
        NOT_D_EQUAL    ( lexeme = "!==", combineDirection = CombineDirection.LEFT  ),

        D_PLUS         ( lexeme = "++" , combineDirection = CombineDirection.LEFT  ),
        D_MINUS        ( lexeme = "--" , combineDirection = CombineDirection.LEFT  ),
        D_STAR         ( lexeme = "**" , combineDirection = CombineDirection.RIGHT ),
        D_NOT          ( lexeme = "!!" , combineDirection = CombineDirection.LEFT  ),

        LESS           ( lexeme = "<"  , combineDirection = CombineDirection.LEFT  ),
        GREATER        ( lexeme = ">"  , combineDirection = CombineDirection.LEFT  ),

        LESS_EQUAL     ( lexeme = "<=" , combineDirection = CombineDirection.LEFT  ),
        GREATER_EQUAL  ( lexeme = ">=" , combineDirection = CombineDirection.LEFT  ),

        AND            ( lexeme = "&&" , combineDirection = CombineDirection.LEFT  ),
        OR             ( lexeme = "||" , combineDirection = CombineDirection.LEFT  ),

        LEFT_PAREN     ( lexeme = "("  , combineDirection = CombineDirection.LEFT  ),
        RIGHT_PAREN    ( lexeme = ")"  , combineDirection = CombineDirection.LEFT  ),
        LEFT_BRACE     ( lexeme = "{"  , combineDirection = CombineDirection.LEFT  ),
        RIGHT_BRACE    ( lexeme = "}"  , combineDirection = CombineDirection.LEFT  ),
        LEFT_BRACKET   ( lexeme = "["  , combineDirection = CombineDirection.LEFT  ),
        RIGHT_BRACKET  ( lexeme = "]"  , combineDirection = CombineDirection.LEFT  ),

        DOT            ( lexeme = "."  , combineDirection = CombineDirection.LEFT  ),
        COMMA          ( lexeme = ","  , combineDirection = CombineDirection.LEFT  ),
        QUESTION_MARK  ( lexeme = "?"  , combineDirection = CombineDirection.LEFT  ),
        SEMICOLON      ( lexeme = ";"  , combineDirection = CombineDirection.LEFT  ),
        COLON          ( lexeme = ":"  , combineDirection = CombineDirection.LEFT  ),
        D_COLON        ( lexeme = "::" , combineDirection = CombineDirection.LEFT  ),

        SINGLE_ARROW   ( lexeme = "->" , combineDirection = CombineDirection.LEFT  ),
        DOUBLE_ARROW   ( lexeme = "=>" , combineDirection = CombineDirection.LEFT  ),

        AT             ( lexeme = "@"  , combineDirection = CombineDirection.LEFT  ),

        BIT_AND        ( lexeme = "&"  , combineDirection = CombineDirection.LEFT  ),
        BIT_OR         ( lexeme = "|"  , combineDirection = CombineDirection.LEFT  ),
        BIT_XOR        ( lexeme = "^"  , combineDirection = CombineDirection.LEFT  ),
        BIT_NOT        ( lexeme = "~"  , combineDirection = CombineDirection.LEFT  ),
        BIT_LEFT       ( lexeme = "<<" , combineDirection = CombineDirection.LEFT  ),
        BIT_RIGHT      ( lexeme = ">>" , combineDirection = CombineDirection.LEFT  ),
        ;

        enum class CombineDirection { LEFT, RIGHT, NONE }
        companion object { val table = values().associateBy { it.lexeme } }
    }

    enum class Keyword: Token {
        NULL       ,
        MAIN       ,

        FOR        ,
        WHILE      ,
        DO         ,

        CONST      ,
        VAL        ,
        VAR        ,

        VARARG     ,

        TRUE       ,
        FALSE      ,

        FUNC       ,
        INLINE     ,
        INFIX      ,

        IF         ,
        ELIF       ,
        ELSE       ,

        RETURN     ,
        BREAK      ,
        CONTINUE   ,

        SWITCH     ,
        WHEN       ,

        IS         ,
        BY         ,
        TO         ,
        SKIP       ,

        DATA       ,
        CLASS      ,
        OBJECT     ,
        ENUM       ,
        INTERFACE  ,
        ABSTRACT   ,

        OPERATOR   ,

        PUBLIC     ,
        PROTECTED  ,
        DEFAULT    ,
        PRIVATE    ,

        FINAL      ,

        THIS       ,
        SUPER      ,

        PACKAGE    ,
        IMPORT     ,
        AS         ,

        OPEN       ,
        OVERRIDE   ,

        ANNOTATION ,

        GET        ,
        SET        ,
        ;
        override val lexeme = name.lowercase()
        companion object { val table = values().associateBy { it.name.lowercase() } }
    }
}
