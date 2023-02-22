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
}

class ImplementedStringLocation(override var index: Int, override var row: Int, override var column: Int) : MutableStringLocation {
    override fun update(location: StringLocation) {
        index = location.index
        row = location.row
        column = location.column
    }
}

fun stringLocationOf(index: Int = 0, row: Int = 0, column: Int = 0): StringLocation = ImplementedStringLocation(index, row, column)
fun mutableStringLocationOf(index: Int = 0, row: Int = 0, column: Int = 0): MutableStringLocation = ImplementedStringLocation(index, row, column)