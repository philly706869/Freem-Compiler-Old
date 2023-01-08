package net.loute.freem.compiler.symbolTable.frontend.token

sealed interface Token {
    object LINEBREAK: Token

    sealed interface PolymorphicToken: Token { val lexeme: String }
    data class IDENTIFIER(override val lexeme: String): PolymorphicToken
    sealed interface Literal: PolymorphicToken {
        sealed interface Number<T : Comparable<T>>: Literal {
            fun toNumber(): T

            /**
             * This interface is for companion objects that implement the Number interface.
             * */
            sealed class NumberCompanion<T>(val suffix: String?, instanceCreator: (lexeme: String) -> T) {
                val suffixRegex = suffix?.let { "$it\$".toRegex(RegexOption.IGNORE_CASE) }
                val createInstance = instanceCreator
            }

            data class BYTE (override val lexeme: String): Number<Byte> {
                companion object: NumberCompanion<BYTE>("b", ::BYTE)
                override fun toNumber() = lexeme.toByte()
            }
            data class SHORT (override val lexeme: String) : Number<Short> {
                companion object: NumberCompanion<SHORT>("s", ::SHORT)
                override fun toNumber() = lexeme.toShort()
            }
            data class INT (override val lexeme: String) : Number<Int> {
                companion object: NumberCompanion<INT>(null, ::INT)
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
                companion object: NumberCompanion<DOUBLE>(null, ::DOUBLE)
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

    enum class Operator(val value: String, val combineDirection: CombineDirection) : Token {
        EQUAL          ( value = "="  , combineDirection = CombineDirection.LEFT  ),
        PLUS           ( value = "+"  , combineDirection = CombineDirection.LEFT  ),
        MINUS          ( value = "-"  , combineDirection = CombineDirection.LEFT  ),
        STAR           ( value = "*"  , combineDirection = CombineDirection.LEFT  ),
        SLASH          ( value = "/"  , combineDirection = CombineDirection.LEFT  ),
        PERCENT        ( value = "%"  , combineDirection = CombineDirection.LEFT  ),
        NOT            ( value = "!"  , combineDirection = CombineDirection.LEFT  ),
        PLUS_EQ        ( value = "+=" , combineDirection = CombineDirection.LEFT  ),
        MINUS_EQ       ( value = "-=" , combineDirection = CombineDirection.LEFT  ),
        STAR_EQ        ( value = "*=" , combineDirection = CombineDirection.LEFT  ),
        SLASH_EQ       ( value = "/=" , combineDirection = CombineDirection.LEFT  ),
        PERCENT_EQ     ( value = "%=" , combineDirection = CombineDirection.LEFT  ),

        D_EQUAL        ( value = "==" , combineDirection = CombineDirection.LEFT  ),
        NOT_EQ         ( value = "!=" , combineDirection = CombineDirection.LEFT  ),
        T_EQUAL        ( value = "===", combineDirection = CombineDirection.LEFT  ),
        NOT_D_EQUAL    ( value = "!==", combineDirection = CombineDirection.LEFT  ),

        D_PLUS         ( value = "++" , combineDirection = CombineDirection.LEFT  ),
        D_MINUS        ( value = "--" , combineDirection = CombineDirection.LEFT  ),
        D_STAR         ( value = "**" , combineDirection = CombineDirection.RIGHT ),
        D_NOT          ( value = "!!" , combineDirection = CombineDirection.LEFT  ),

        LESS           ( value = "<"  , combineDirection = CombineDirection.LEFT  ),
        GREATER        ( value = ">"  , combineDirection = CombineDirection.LEFT  ),

        LESS_EQUAL     ( value = "<=" , combineDirection = CombineDirection.LEFT  ),
        GREATER_EQUAL  ( value = ">=" , combineDirection = CombineDirection.LEFT  ),

        AND            ( value = "&&" , combineDirection = CombineDirection.LEFT  ),
        OR             ( value = "||" , combineDirection = CombineDirection.LEFT  ),

        LEFT_PAREN     ( value = "("  , combineDirection = CombineDirection.LEFT  ),
        RIGHT_PAREN    ( value = ")"  , combineDirection = CombineDirection.LEFT  ),
        LEFT_BRACE     ( value = "{"  , combineDirection = CombineDirection.LEFT  ),
        RIGHT_BRACE    ( value = "}"  , combineDirection = CombineDirection.LEFT  ),
        LEFT_BRACKET   ( value = "["  , combineDirection = CombineDirection.LEFT  ),
        RIGHT_BRACKET  ( value = "]"  , combineDirection = CombineDirection.LEFT  ),

        DOT            ( value = "."  , combineDirection = CombineDirection.LEFT  ),
        COMMA          ( value = ","  , combineDirection = CombineDirection.LEFT  ),
        QUESTION_MARK  ( value = "?"  , combineDirection = CombineDirection.LEFT  ),
        SEMICOLON      ( value = ";"  , combineDirection = CombineDirection.LEFT  ),
        COLON          ( value = ":"  , combineDirection = CombineDirection.LEFT  ),
        D_COLON        ( value = "::" , combineDirection = CombineDirection.LEFT  ),

        SINGLE_ARROW   ( value = "->" , combineDirection = CombineDirection.LEFT  ),
        DOUBLE_ARROW   ( value = "=>" , combineDirection = CombineDirection.LEFT  ),

        AT             ( value = "@"  , combineDirection = CombineDirection.LEFT  ),

        BIT_AND        ( value = "&"  , combineDirection = CombineDirection.LEFT  ),
        BIT_OR         ( value = "|"  , combineDirection = CombineDirection.LEFT  ),
        BIT_XOR        ( value = "^"  , combineDirection = CombineDirection.LEFT  ),
        BIT_NOT        ( value = "~"  , combineDirection = CombineDirection.LEFT  ),
        BIT_LEFT       ( value = "<<" , combineDirection = CombineDirection.LEFT  ),
        BIT_RIGHT      ( value = ">>" , combineDirection = CombineDirection.LEFT  ),
        ;

        enum class CombineDirection { LEFT, RIGHT, NONE }
        companion object { val table = values().associateBy { it.value } }
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

        override fun toString() = this.name.lowercase()
        companion object {
            val table = values().associateBy { it.name.lowercase() }
        }
    }
}
