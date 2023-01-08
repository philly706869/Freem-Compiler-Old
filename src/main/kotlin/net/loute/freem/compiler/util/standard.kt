package net.loute.freem.compiler.util

inline fun Boolean.then(block: () -> Unit) = run { if (this) block(); this }
inline fun Boolean.but(block: () -> Unit) = !this.then(block)
inline fun <T: Number> T.then(block: () -> Unit) = run { if (this != 0) block(); this }
inline fun <T: Number> T.but(block: () -> Unit) = run { if (this == 0) block(); this }
inline fun <T> T?.safe(block: T.() -> Unit) = (this != null).then { this!!.block() }
inline infix fun <T, R> T.pipe(function: (T) -> R) = function(this)
fun Boolean.toByte(): Byte = if (this) 1 else 0
fun Boolean.toShort(): Short = if (this) 1 else 0
fun Boolean.toInt(): Int = if (this) 1 else 0
fun Boolean.toLong(): Long = if (this) 1 else 0
fun Boolean.toFloat(): Float = if (this) 1f else 0f
fun Boolean.toDouble(): Double = if (this) 1.0 else 0.0
fun Boolean.toUByte(): UByte = if (this) 1u else 0u
fun Boolean.toUShort(): UShort = if (this) 1u else 0u
fun Boolean.toUInt(): UInt = if (this) 1u else 0u
fun Boolean.toULong(): ULong = if (this) 1u else 0u
fun Number.toBoolean() = this != 0