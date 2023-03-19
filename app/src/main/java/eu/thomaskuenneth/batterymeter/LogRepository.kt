package eu.thomaskuenneth.batterymeter

import android.content.Context
import java.util.Date

private const val FILENAME = "log.txt"

class LogRepository(private val context: Context) {

    fun appendTextToFile(prefix: String) {
        context.appendTextToFile(prefix)
    }

    fun readFile() = context.readFile()
}

fun Context.appendTextToFile(prefix: String) {
    openFileOutput(FILENAME, Context.MODE_APPEND).use {
        it.bufferedWriter().use { writer ->
            writer.write("$prefix called at ${Date()}")
            writer.newLine()
        }
    }
}

fun Context.readFile(): List<String> {
    return openFileInput(FILENAME).use {
        it.bufferedReader().readLines()
    }
}
