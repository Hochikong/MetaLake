package me.ckhoidea.lakeplugin

import java.io.PrintWriter
import java.io.StringWriter

object Utils {
    fun exceptionToString(): Pair<StringWriter, PrintWriter> {
        val sw = StringWriter();
        return Pair(sw, PrintWriter(sw))
    }
}