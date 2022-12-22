package net.loute.freem.compiler.util

inline fun Boolean.then(block: () -> Unit) = run { if (this) block(); this }
inline fun Boolean.but(block: () -> Unit) = !this.then(block)
inline fun <T: Number> T.then(block: () -> Unit) = run { if (this != 0) block(); this }
inline fun <T: Number> T.but(block: () -> Unit) = run { if (this == 0) block(); this }
inline fun <T> T?.safe(block: T.() -> Unit) = (this != null).then { this!!.block() }
inline infix fun <T, R> T.pipe(function: (T) -> R) = function(this)