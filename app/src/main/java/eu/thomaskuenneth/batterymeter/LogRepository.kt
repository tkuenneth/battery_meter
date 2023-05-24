package eu.thomaskuenneth.batterymeter

import android.content.Context
import java.text.DateFormat
import java.util.Date

private const val FILENAME = "log.txt"

class LogRepository(private val context: Context) {

    fun appendTextToFile(prefix: String) {
        context.appendTextToFile(prefix)
    }

    fun readFile() = context.readFile()
}

fun Context.appendTextToFile(
    prefix: String,
    now: Long = System.currentTimeMillis()
) {
    openFileOutput(FILENAME, Context.MODE_APPEND).use {
        it.bufferedWriter().use { writer ->
            val df = DateFormat.getInstance()
            writer.write("$prefix called at ${df.format(Date(now))}")
            writer.newLine()
        }
    }
}

fun Context.readFile(): List<String> {
    return openFileInput(FILENAME).use {
        it.bufferedReader().readLines()
    }
}
