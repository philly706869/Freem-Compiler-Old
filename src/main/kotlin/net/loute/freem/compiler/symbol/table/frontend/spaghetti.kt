package net.loute.freem.compiler.symbol.table.frontend

import java.io.File
import kotlin.system.measureNanoTime

//
// pre implement code
//

fun main() {
    val elapsed = measureNanoTime(::run)
    println("elapsed: ${elapsed / 1000000}ms($elapsed)")
}

const val defaultProjectPath = "src/test/resources/sample-project"
const val defaultFile = "src/test/resources/sample-code.fr"

fun run() {
    val code = File(defaultFile).also { require(it.isFile) }.readText()
    val projectDir = File(defaultProjectPath).also { require(it.isDirectory) }

    val sourceFilePaths: List<String> = run {
        val ret = mutableListOf<String>()

        fun flat(dir: File) {
            val list = dir.listFiles()?:return
            for (i in list) {
                if (i.isDirectory) flat(i)
                else ret += i.path
            }
        }
        flat(projectDir)

        ret
    }
    sourceFilePaths.forEach(::println)
    println()
}

fun <T>tmp(context: PatternContext<T>) {}

class Matcher<T> {
    fun match(target: List<T>): Boolean {
        return false
    }
}

typealias PatternContext<T> = PatternContextStruct<T>.() -> Pattern<T>
interface PatternContextStruct<T> {
    fun state(block: (T) -> Boolean): Condition<T> = Condition(block)

    fun Pattern<T>.quan(min: Int, max: Int): Quantity<T> = Quantity(min, max, this)
    fun Pattern<T>.quan(min: Int): Quantity<T> = Quantity(min, null, this)
    val Pattern<T>.opt: Optional<T> get() = Optional(this)
    val Pattern<T>.plus: Plus<T> get() = Plus(this)
    val Pattern<T>.star: Star<T> get() = Star(this)

    operator fun Pattern<T>.plus(pattern: Pattern<T>): Capture<T> = Capture(this, pattern)

    fun T.quan(min: Int, max: Int): Quantity<T> = Value(this).quan(min, max)
    fun T.quan(min: Int): Quantity<T> = Value(this).quan(min)
    val T.opt: Optional<T> get() = Value(this).opt
    val T.plus: Plus<T> get() = Value(this).plus
    val T.star: Star<T> get() = Value(this).star
}

sealed interface Pattern<T>
sealed interface CPattern<T>: Pattern<T> {
    val child: Pattern<T>
}

class Capture<T>(val first: Pattern<T>, val second: Pattern<T>): Pattern<T>

class Condition<T>(val block: (T) -> Boolean): Pattern<T>
class Quantity<T>(val min: Int, val max: Int?, override val child: Pattern<T>): CPattern<T>
class Optional<T>(override val child: Pattern<T>): CPattern<T>
class Plus<T>(override val child: Pattern<T>): CPattern<T>
class Star<T>(override val child: Pattern<T>): CPattern<T>

class Value<T>(val value: T): Pattern<T>