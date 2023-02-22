package net.loute.freem.compiler.util.range

import net.loute.freem.compiler.util.location.*

interface Range {
    val start: Location
    val end: Location
}
interface MutableRange<R: Range>: Range {
    override val start: MutableLocation<*>
    override val end: MutableLocation<*>

    fun update(range: R)
}
