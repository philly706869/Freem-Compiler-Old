package net.loute.freem.compiler.symbol.table.frontend

import net.loute.freem.compiler.util.isAlpha
import java.io.File
import kotlin.system.measureNanoTime

//
// pre implement code
//

fun main() {
    val elapsed = measureNanoTime(::run)
    println("elapsed: ${elapsed / 1_000_000}ms($elapsed)")
}

fun run() {
    val code = File("src/test/resources/sample-code.fr").readText()
    val projectDir = File("src/test/resources/sample-project")

    val sourceFilePaths: List<String> = run {
        fun flat(dir: File): List<String> {
            return dir.listFiles()?.flatMap {
                if (it.isDirectory) flat(it)
                else listOf(it.path)
            } ?: emptyList()
        }
        flat(projectDir)
    }
    sourceFilePaths.forEach(::println)
    println()

}

fun a(block: TextRegexContext.() -> Unit) {}

fun b() {
    a {
        val space = Char::isWhitespace or '\n'
        val spaceAble = space.star
        val spaceMust = space.plus

        val identify = (Char::isAlpha or '_') and (Char::isAlpha or '_' or Char::isDigit).star

        r q spaceAble
        r q "package" /*when does not match: maybe package*/
        r q spaceMust
        r q (identify and (spaceAble and '.' and spaceAble and identify).star)
        r q spaceAble
    }
}

interface RegexContext<T> {

    val r get() = Require

    object Require

    infix fun Require           .q(pattern: Pattern<T>)



    infix fun ((T) -> Boolean)  .or(condition: (T) -> Boolean)  : Or<T>
    infix fun ((T) -> Boolean)  .or(pattern: Pattern<T>)        : Or<T>
    infix fun ((T) -> Boolean)  .or(value: T)                   : Or<T>

    infix fun Pattern<T>        .or(condition: (T) -> Boolean)  : Or<T>
    infix fun Pattern<T>        .or(pattern: Pattern<T>)        : Or<T>
    infix fun Pattern<T>        .or(value: T)                   : Or<T>

    infix fun T                 .or(condition: (T) -> Boolean)  : Or<T>
    infix fun T                 .or(pattern: Pattern<T>)        : Or<T>
    infix fun T                 .or(value: T)                   : Or<T>



    infix fun ((T) -> Boolean)  .and(condition: (T) -> Boolean) : And<T>
    infix fun ((T) -> Boolean)  .and(pattern: Pattern<T>)       : And<T>
    infix fun ((T) -> Boolean)  .and(value: T)                  : And<T>

    infix fun Pattern<T>        .and(condition: (T) -> Boolean) : And<T>
    infix fun Pattern<T>        .and(pattern: Pattern<T>)       : And<T>
    infix fun Pattern<T>        .and(value: T)                  : And<T>

    infix fun T                 .and(condition: (T) -> Boolean) : And<T>
    infix fun T                 .and(pattern: Pattern<T>)       : And<T>
    infix fun T                 .and(value: T)                  : And<T>



    val ((T) -> Boolean)        .plus                           : Plus<T>
    val Pattern<T>              .plus                           : Plus<T>
    val T                       .plus                           : Plus<T>

    val ((T) -> Boolean)        .star                           : Star<T>
    val Pattern<T>              .star                           : Star<T>
    val T                       .star                           : Star<T>

    val ((T) -> Boolean)        .opt                            : Opt<T>
    val Pattern<T>              .opt                            : Opt<T>
    val T                       .opt                            : Opt<T>



    fun ((T) -> Boolean)        .quan(min: Int, max: Int)       : Quan<T>
    fun Pattern<T>              .quan(min: Int, max: Int)       : Quan<T>
    fun T                       .quan(min: Int, max: Int)       : Quan<T>

    fun ((T) -> Boolean)        .quan(max: Int)                 : Quan<T>
    fun Pattern<T>              .quan(max: Int)                 : Quan<T>
    fun T                       .quan(max: Int)                 : Quan<T>

}

interface Pattern<T>

interface Or<T>                 : Pattern<T>
interface And<T>                : Pattern<T>

interface Plus<T>               : Pattern<T>
interface Star<T>               : Pattern<T>
interface Opt<T>                : Pattern<T>
interface Quan<T>               : Pattern<T>



interface TextRegexContext: RegexContext<Char> {
    infix fun RegexContext.Require.q(string: String)
    infix fun RegexContext.Require.q(char: Char)
    infix fun RegexContext.Require.q(pattern: TextPattern)
}

interface TextPattern: Pattern<Char>

// <s> ::=[\s\n]*
// <id> ::=[a-zA-Z_]\w*
// <program> ::=<s><package><s><id><s>(\.<s><id><s>)*
/*
regex {
    val space = ( regex.space / regex.linebreak ).star
    val ident = ( a-zA-Z_, regex.word ).star

}
 */

//fun <T>tmp(context: PatternContext<T>) {}
//
//class Matcher<T> {
//    fun match(target: List<T>): Boolean {
//        return false
//    }
//}
//
//typealias PatternContext<T> = PatternContextStruct<T>.() -> Pattern<T>
//interface PatternContextStruct<T> {
//    fun state(block: (T) -> Boolean): Condition<T> = Condition(block)
//
//    fun Pattern<T>.quan(min: Int, max: Int): Quantity<T> = Quantity(min, max, this)
//    fun Pattern<T>.quan(min: Int): Quantity<T> = Quantity(min, null, this)
//    val Pattern<T>.opt: Optional<T> get() = Optional(this)
//    val Pattern<T>.plus: Plus<T> get() = Plus(this)
//    val Pattern<T>.star: Star<T> get() = Star(this)
//
//    operator fun Pattern<T>.plus(pattern: Pattern<T>): Capture<T> = Capture(this, pattern)
//
//    fun T.quan(min: Int, max: Int): Quantity<T> = Value(this).quan(min, max)
//    fun T.quan(min: Int): Quantity<T> = Value(this).quan(min)
//    val T.opt: Optional<T> get() = Value(this).opt
//    val T.plus: Plus<T> get() = Value(this).plus
//    val T.star: Star<T> get() = Value(this).star
//}
//
//sealed interface Pattern<T>
//sealed interface CPattern<T>: Pattern<T> {
//    val child: Pattern<T>
//}
//
//class Capture<T>(val first: Pattern<T>, val second: Pattern<T>): Pattern<T>
//
//class Condition<T>(val block: (T) -> Boolean): Pattern<T>
//class Quantity<T>(val min: Int, val max: Int?, override val child: Pattern<T>): CPattern<T>
//class Optional<T>(override val child: Pattern<T>): CPattern<T>
//class Plus<T>(override val child: Pattern<T>): CPattern<T>
//class Star<T>(override val child: Pattern<T>): CPattern<T>
//
//class Value<T>(val value: T): Pattern<T>