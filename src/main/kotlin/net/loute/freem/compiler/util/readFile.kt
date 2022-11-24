package net.loute.freem.compiler.util

import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

fun readFile(file: File) = run {
    if (!file.exists() && !file.isFile) throw FileNotFoundException("File does not exist")
    FileReader(file).use {
        val chars = CharArray(file.length().toInt())
        it.read(chars)
        String(chars)
    }
}
