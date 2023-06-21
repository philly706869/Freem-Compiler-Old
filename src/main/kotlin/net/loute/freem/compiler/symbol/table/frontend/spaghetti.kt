package net.loute.freem.compiler.symbol.table.frontend

import kotlinx.coroutines.processNextEventInCurrentThread
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

/*

 */

fun <T>test/*test*/(t: T): Unit {}

/*

func <T: Type> identifier(arg: Type) = arg
func <T: Type> identifier(arg: Type) println(arg)
func identifier(arg: Type)

 */


class FrontEndAnalyzer(private val iterator: Iterator<Char>, val file: File? = null): TextMatcher() {
    constructor(iterable: Iterable<Char>, file: File? = null): this(iterable.iterator(), file)
    constructor(string: String, file: File? = null): this(string.iterator(), file)


    val expctdMsg: (event: MatcherEvent<Char>) -> Unit = { event -> println("expected ${event.expected}") }

    private val s = (Char::isWhitespace or ("//" and any.lazy and '\n') or ("/*" and any.lazy and "*/")).plus
    private val sa = s.opt

    private val identifier = (Char::isLetter or '_') and (Char::isLetterOrDigit or '_').star
    private val packageName = (identifier and (sa and '.' and sa and identifier).star)



    private val expression: Pattern<Char> = TODO()

    private val scope =
                '{'.on("fail", expctdMsg) and
                sa and
                { false } and
                sa and
                '}'.on("fail", expctdMsg)

    private val parameterDefine = '(' and sa and expression and sa and (',' and sa and expression and sa).star and ','.opt and sa and ')'
    private val functionDefine = "func" and s and identifier and sa and parameterDefine and (sa and ':' and sa and identifier).opt and sa and scope

    override val rootPattern = sa and
            "package" and s and packageName and s and
            ("import" and s and packageName and ((sa and '.' and sa and '*') or (s and "as" and s and identifier)).opt and s).star and

}

data class MatcherEvent<T>(val isSucceeded: Boolean, val index: Int, val expected: Pattern<Char>)

abstract class Matcher<T> {

    protected abstract val rootPattern: Pattern<T>

    fun match(input: T): Boolean {
        TODO()
    }

    protected val any = { _: T -> true }.toPattern()



    protected fun ((T) -> Boolean).toPattern(): Pattern<T>  = TODO()
    protected fun T.toPattern(): Pattern<T>                 = TODO()



    protected infix fun ((T) -> Boolean).or(condition: (T) -> Boolean): Or<T>   = or(this.toPattern(), condition.toPattern())
    protected infix fun ((T) -> Boolean).or(pattern: Pattern<T>): Or<T>         = or(this.toPattern(), pattern)
    protected infix fun ((T) -> Boolean).or(value: T): Or<T>                    = or(this.toPattern(), value.toPattern())

    protected infix fun Pattern<T>.or(condition: (T) -> Boolean): Or<T>         = or(this, condition.toPattern())
    protected infix fun Pattern<T>.or(pattern: Pattern<T>): Or<T>               = or(this, pattern)
    protected infix fun Pattern<T>.or(value: T): Or<T>                          = or(this, value.toPattern())

    protected infix fun T.or(condition: (T) -> Boolean): Or<T>                  = or(this.toPattern(), condition.toPattern())
    protected infix fun T.or(pattern: Pattern<T>): Or<T>                        = or(this.toPattern(), pattern)
    protected infix fun T.or(value: T): Or<T>                                   = or(this.toPattern(), value.toPattern())

    private fun or(pattern: Pattern<T>, input: Pattern<T>): Or<T> {
        TODO()
    }

    protected infix fun ((T) -> Boolean).and(condition: (T) -> Boolean): And<T> = and(this.toPattern(), condition.toPattern())
    protected infix fun ((T) -> Boolean).and(pattern: Pattern<T>): And<T>       = and(this.toPattern(), pattern)
    protected infix fun ((T) -> Boolean).and(value: T): And<T>                  = and(this.toPattern(), value.toPattern())

    protected infix fun Pattern<T>.and(condition: (T) -> Boolean): And<T>       = and(this, condition.toPattern())
    protected infix fun Pattern<T>.and(pattern: Pattern<T>): And<T>             = and(this, pattern)
    protected infix fun Pattern<T>.and(value: T): And<T>                        = and(this, value.toPattern())

    protected infix fun T.and(condition: (T) -> Boolean): And<T>                = and(this.toPattern(), condition.toPattern())
    protected infix fun T.and(pattern: Pattern<T>): And<T>                      = and(this.toPattern(), pattern)
    protected infix fun T.and(value: T): And<T>                                 = and(this.toPattern(), value.toPattern())

    private fun and(pattern: Pattern<T>, input: Pattern<T>): And<T> {
        TODO()
    }

    protected val ((T) -> Boolean).plus: Plus<T> get()  = plus(this.toPattern())
    protected val Pattern<T>.plus: Plus<T> get()        = plus(this)
    protected val T.plus: Plus<T> get()                 = plus(this.toPattern())

    private fun plus(pattern: Pattern<T>): Plus<T> {
        TODO()
    }

    protected val ((T) -> Boolean).star: Star<T> get()  = star(this.toPattern())
    protected val Pattern<T>.star: Star<T> get()        = star(this)
    protected val T.star: Star<T> get()                 = star(this.toPattern())

    private fun star(pattern: Pattern<T>): Star<T> {
        TODO()
    }

    protected val ((T) -> Boolean).opt: Opt<T> get()    = opt(this.toPattern())
    protected val Pattern<T>.opt: Opt<T> get()          = opt(this)
    protected val T.opt: Opt<T> get()                   = opt(this.toPattern())

    private fun opt(pattern: Pattern<T>): Opt<T> {
        TODO()
    }

    protected val ((T) -> Boolean).lazy: Lazy<T> get()  = lazy(this.toPattern())
    protected val Pattern<T>.lazy: Lazy<T> get()        = lazy(this)
    protected val T.lazy: Lazy<T> get()                 = lazy(this.toPattern())

    private fun lazy(pattern: Pattern<T>): Lazy<T> {
        TODO()
    }

    protected fun ((T) -> Boolean).quan(min: Int, max: Int): Quan<T>    = quan(this.toPattern(), min, max)
    protected fun ((T) -> Boolean).quan(min: Int): Quan<T>              = quan(this.toPattern(), min, null)
    protected fun Pattern<T>.quan(min: Int, max: Int): Quan<T>          = quan(this, min, max)
    protected fun Pattern<T>.quan(min: Int): Quan<T>                    = quan(this, min, null)
    protected fun T.quan(min: Int, max: Int): Quan<T>                   = quan(this.toPattern(), min, max)
    protected fun T.quan(min: Int): Quan<T>                             = quan(this.toPattern(), min, null)

    private fun quan(pattern: Pattern<T>, min: Int, max: Int?): Quan<T> {
        TODO()
    }

    protected fun ((T) -> Boolean).on(event: String, block: (event: MatcherEvent<T>) -> Unit): Pattern<T>   = on(this.toPattern(), event, block)
    protected fun Pattern<T>.on(event: String, block: (event: MatcherEvent<T>) -> Unit): Pattern<T>         = on(this, event, block)
    protected fun T.on(event: String, block: (event: MatcherEvent<T>) -> Unit): Pattern<T>                  = on(this.toPattern(), event, block)

    private fun on(pattern: Pattern<T>, event: String, block: (event: MatcherEvent<T>) -> Unit): Pattern<T> {
        TODO()
    }

}

abstract class TextMatcher: Matcher<Char>() {

    protected fun String.toPattern(): Pattern<Char> = TODO()

    protected infix fun ((Char) -> Boolean).or(value: String): Or<Char>     = or(this.toPattern(), value.toPattern())
    protected infix fun Pattern<Char>.or(value: String): Or<Char>           = or(this, value.toPattern())
    protected infix fun Char.or(value: String): Or<Char>                    = or(this.toPattern(), value.toPattern())
    protected infix fun String.or(condition: (Char) -> Boolean): Or<Char>   = or(this.toPattern(), condition.toPattern())
    protected infix fun String.or(pattern: Pattern<Char>): Or<Char>         = or(this.toPattern(), pattern)
    protected infix fun String.or(value: Char): Or<Char>                    = or(this.toPattern(), value.toPattern())
    protected infix fun String.or(value: String): Or<Char>                  = or(this.toPattern(), value.toPattern())

    private fun or(pattern: Pattern<Char>, input: Pattern<Char>): Or<Char> {
        TODO()
    }

    protected infix fun ((Char) -> Boolean).and(value: String): And<Char>   = and(this.toPattern(), value.toPattern())
    protected infix fun Pattern<Char>.and(value: String): And<Char>         = and(this, value.toPattern())
    protected infix fun Char.and(value: String): And<Char>                  = and(this.toPattern(), value.toPattern())
    protected infix fun String.and(condition: (Char) -> Boolean): And<Char> = and(this.toPattern(), condition.toPattern())
    protected infix fun String.and(pattern: Pattern<Char>): And<Char>       = and(this.toPattern(), pattern)
    protected infix fun String.and(value: Char): And<Char>                  = and(this.toPattern(), value.toPattern())
    protected infix fun String.and(value: String): And<Char>                = and(this.toPattern(), value.toPattern())

    private fun and(pattern: Pattern<Char>, input: Pattern<Char>): And<Char> {
        TODO()
    }

    protected val String.plus: Plus<Char> get() = TODO()
    protected val String.star: Star<Char> get() = TODO()
    protected val String.opt: Opt<Char> get() = TODO()
    protected val String.lazy: Lazy<Char> get() = TODO()

    protected fun String.quan(min: Int, max: Int): Quan<Char> = TODO()
    protected fun String.quan(max: Int): Quan<Char> = TODO()

    protected fun String.on(event: String, block: (event: MatcherEvent<Char>) -> Unit): Pattern<Char> = TODO()
}

interface Pattern<T>

interface Or<T>: Pattern<T>
interface And<T>: Pattern<T>

interface Plus<T>: Pattern<T>
interface Star<T>: Pattern<T>
interface Opt<T>: Pattern<T>
interface Quan<T>: Pattern<T>
interface Lazy<T>: Pattern<T>

