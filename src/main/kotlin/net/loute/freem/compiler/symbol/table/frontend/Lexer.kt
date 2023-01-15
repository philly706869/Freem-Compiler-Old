package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.CompileException
import net.loute.freem.compiler.symbol.table.frontend.token.Token

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

object Lexer {
    fun lexicalAnalyse(code: String, pathname: String? = null): Array<Token> {
        val tokenArray = ArrayList<Token>() // final result
        var start = 0 // foot of code
        var current  = 0 // head of code
        var line = 1 // line of code


        /**
         * next character
         * */
        fun advance() = code[current++]
        fun peek() = code[current]
        fun isEnd() = current >= code.length
        fun isNotEnd() = current < code.length

        while (!isEnd()) {
            var char = advance()

        }

        /*
        var trie = Token.Operator.trie
        var str = ""
        char = code[current]
        while (char in trie) {
            str += char
            trie = trie[char]!!
            current++
            char = code[current]
        }
        println(str)
        */

        // https://github.com/holgerbrandl/krangl looks sweet
        tokenArray.add(Token.EOF) //
        return tokenArray.toTypedArray() // toTypedArray for MutableArray to Array
    }
}
