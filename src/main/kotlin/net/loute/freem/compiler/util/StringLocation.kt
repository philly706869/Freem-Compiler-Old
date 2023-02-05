package net.loute.freem.compiler.util

data class StringLocation(val index: UInt, val row: UInt, val column: UInt) {
    operator fun plus(string: String) =
        run {
            val lineCount = string.count { it == '\n' }
            StringLocation(
                index + string.length.toUInt(),
                row + (lineCount - 1).coerceAtLeast(0).toUInt(),
                column + (
                        if (lineCount != 0) string.substringAfterLast('\n').length
                        else string.length
                        ).toUInt()
            )
        }
}
