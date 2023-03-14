package net.loute.freem.compiler.util.location

interface StringLocation: Location {
    val index: Int
    val row: Int
    val column: Int
}
interface MutableStringLocation: StringLocation, MutableLocation<StringLocation> {
    override var index: Int
    override var row: Int
    override var column: Int

    override fun update(location: StringLocation)
    fun update(index: Int?, row: Int?, column: Int?)
    fun add(string: String)
    fun add(char: Char)
}

class ImplementedStringLocation(override var index: Int, override var row: Int, override var column: Int) : MutableStringLocation {
    override fun update(location: StringLocation) {
        index = location.index
        row = location.row
        column = location.column
    }

    override fun update(index: Int?, row: Int?, column: Int?) {
        this.index = index?:this.index
        this.row = row?:this.row
        this.column = column?:this.column
    }

    override fun add(string: String) = string.forEach(::add)

    override fun add(char: Char) {
        if (char == '\n') {
            row++
            column = 0
        }
        column++
        index++
    }
}

fun stringLocationOf(index: Int = 0, row: Int = 0, column: Int = 0): StringLocation = ImplementedStringLocation(index, row, column)
fun mutableStringLocationOf(index: Int = 0, row: Int = 0, column: Int = 0): MutableStringLocation = ImplementedStringLocation(index, row, column)