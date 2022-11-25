package net.loute.freem.compiler.util

inline fun Boolean.then(block: () -> Unit) = run { if (this) block(); this }
inline fun <T> T?.safe(block: T.() -> Unit) = (this != null).then { this!!.block() }