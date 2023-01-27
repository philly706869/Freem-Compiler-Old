package net.loute.freem.compiler.main

object MESSAGE {

const val OPTIONS =
"""
flags:
    -encoding <encoding>                    Specify character encoding used by source files
        encoding:
            ISO-8859-1
            US-ASCII
            UTF-8
            UTF-16
            UTF-32
            UTF-16BE
            UTF-16LE
            UTF-32BE
            UTF-32LE
    -v | --version                          
"""

const val HELP =
"""   
Usage: java -jar frc <flags>
$OPTIONS
"""

}