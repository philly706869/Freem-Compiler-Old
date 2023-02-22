package net.loute.freem.compiler.util.range

import net.loute.freem.compiler.util.location.MutableStringLocation
import net.loute.freem.compiler.util.location.StringLocation
import net.loute.freem.compiler.util.location.mutableStringLocationOf

interface StringRange: Range {
    override val end: StringLocation
    override val start: StringLocation
}
interface MutableStringRange: StringRange, MutableRange<StringRange> {
    override val start: MutableStringLocation
    override val end: MutableStringLocation
}

class ImplementedStringRange(override val start: MutableStringLocation, override val end: MutableStringLocation) : MutableStringRange {
    override fun update(range: StringRange) {
        start.update(range.start)
        end.update(range.end)
    }
}

fun stringRangeOf(start: StringLocation, end: StringLocation): StringRange =
    ImplementedStringRange(
        start as MutableStringLocation,
        end as MutableStringLocation
    )
fun stringRangeOf(startIndex: Int = 0, startRow: Int = 0, startColumn: Int = 0, endIndex: Int = 0, endRow: Int = 0, endColumn: Int = 0): StringRange =
    ImplementedStringRange(
        mutableStringLocationOf(
            startIndex,
            startRow,
            startColumn
        ),
        mutableStringLocationOf(
            endIndex,
            endRow,
            endColumn
        )
    )
fun mutableStringRangeOf(start: StringLocation, end: StringLocation): MutableStringRange =
    ImplementedStringRange(
        start as MutableStringLocation,
        end as MutableStringLocation
    )
fun mutableStringRangeOf(startIndex: Int = 0, startRow: Int = 0, startColumn: Int = 0, endIndex: Int = 0, endRow: Int = 0, endColumn: Int = 0) =
    ImplementedStringRange(
        mutableStringLocationOf(
            startIndex,
            startRow,
            startColumn
        ),
        mutableStringLocationOf(
            endIndex,
            endRow,
            endColumn
        )
    )