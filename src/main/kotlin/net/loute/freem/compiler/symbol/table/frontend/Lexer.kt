package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.CompileException
//import net.loute.freem.compiler.symbol.table.frontend.token.Token

/*
fun CharSequence.takeWhile(predicate: (String, Char) -> Boolean): String {
    var acc = ""
    takeWhile { char ->
        predicate(acc, char).also {
            if (it) acc += char
        }
    }
    return acc
}
*/

//class LoopContext {
//    var shouldLoop = true
//    val user = ForUser()
//    inner class ForUser {
//        val break_: Unit get() {
//            shouldLoop = false
//        }
//    }
//}
//inline fun loop(block: LoopContext.ForUser.() -> Unit) {
//    val ctx = LoopContext()
//    while (ctx.shouldLoop) block(ctx.user)
//}
//class Token {
//    enum class Type: TokenType {
//        PLUS() {
//
//        }
//    }
//}

object Lexer {
    //val rule = flow()

    fun lexicalAnalyse(code: String, pathname: String? = null): Array<Token> {
//        val tokenArray = ArrayList<Token>() // final result
//        var start = 0 // foot of code
//        var current  = 0 // head of code
//        var line = 1 // line of code
//
//        /**
//         * @return next character
//         * */
//        fun advance() = code.getOrNull(current++)
//        /**
//         * @return current character
//         */
//        fun peek() = code[current]
//        fun isEnd() = current >= code.length
//        fun isNotEnd() = current < code.length
//
//        while (!isEnd()) {
//            var char = advance()!!
//            // var trie = Token.immutableTokenTrie
//
//
////            while (char in trie) {
////
////            }
//
//            when {
//
//                else -> throw CompileException("")
//            }
//        }
//
//        // tokenArray.add(Token.EOF) // End of File
//        return tokenArray.toTypedArray() // toTypedArray for MutableArray to Array
    }
}
