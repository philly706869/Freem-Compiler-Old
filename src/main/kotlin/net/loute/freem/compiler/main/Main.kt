//package net.loute.freem.compiler.main
//
//import net.loute.freem.compiler.util.CompileException
//
//fun main(args: Array<String>) {
//    try {
//        val options = arrayOf<Option>(
//            Option("encoding", 1, "")
//                    /*
//                    ISO-8859-1
//                    US-ASCII
//                    UTF-8
//                    UTF-16
//                    UTF-32
//                    UTF-16BE
//                    UTF-16LE
//                    UTF-32BE
//                    UTF-32LE
//                    */
//        ).associateBy { it.name }
//
//        val iterator = args.iterator()
//        if (!iterator.hasNext()) throw CompileException(MESSAGE.HELP)
//        val pathname = iterator.next()
//
//        val option = mutableMapOf<String, Array<String>>()
//
//        while (iterator.hasNext()) {
//            val option = iterator.next()
//            if (option.startsWith("-")) {
//                //while ()
//            }
//        }
//
//        //FreemCompiler.compile(pathname)
//    } catch (e: CompileException) { e.printError() }
//}
//
//data class Option {
//    val name: String
//    val valueCount: Int
//    val valueOptions: List<Regex>?
//
//    constructor(name: String, valueCount: Int, valueOptions: Array<String>? = null) {
//        this.name = name
//        this.valueCount = valueCount
//        this.valueOptions = valueOptions?.map { it.toRegex(RegexOption.IGNORE_CASE) }
//    }
//
//    constructor(name: String, valueCount: Int, vararg valueOptions: String)
//            : this(name, valueCount, (valueOptions as Array<String>).map { it.toRegex(RegexOption.IGNORE_CASE) }.ifEmpty { null })
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Option
//
//        if (name != other.name) return false
//        if (valueCount != other.valueCount) return false
//        if (valueOptions != null) {
//            if (other.valueOptions == null) return false
//            if (!valueOptions.contentEquals(other.valueOptions)) return false
//        } else if (other.valueOptions != null) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = name.hashCode()
//        result = 31 * result + valueCount
//        result = 31 * result + (valueOptions?.contentHashCode() ?: 0)
//        return result
//    }
//}
//
//operator fun Boolean.invoke(block: () -> Unit) =
//    this.also {
//        if (this) block()
//    }
