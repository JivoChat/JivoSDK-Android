package com.jivosite.sdk.support.log

import android.content.Context
import android.util.Log
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.support.log.FileLogTree.Companion.ANDROID_LOG_FILE_FORMAT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

/**
 * Created on 27.09.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class FileLogTree(private val context: Context) : Timber.Tree() {

    companion object {
        const val LOGS_DIR = "logs"
        const val ANDROID_LOG_TIME_FORMAT = "MM.dd.yyyy HH:mm:ss:SSS"
        const val ANDROID_LOG_FILE_FORMAT = "dd-MM-yyyy"
    }

    private val writeFile = AtomicReference<File>()

    private val createJob = SupervisorJob()
    private val coroutineScope: CoroutineScope = CoroutineScope(
        defaultContext() + createJob
    )

    private fun defaultContext() = Dispatchers.Default

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.DEBUG) {
            return
        }

        try {
            coroutineScope.launch {
                writeFile.lazySet(context.createFile(createFileName()))
                val result = runCatching {
                    FileOutputStream(
                        writeFile.get(),
                        true
                    ).write("${convertLongToTime(System.currentTimeMillis())} ${convertPriorityToTag(priority)}: $message \n".toByteArray())
                }
                if (result.isFailure) {
                    result.exceptionOrNull()?.printStackTrace()
                }
            }


        } catch (e: IOException) {
            Jivo.e("Error while logging into file: $e")
        }
    }

    private fun convertLongToTime(long: Long): String {
        val date = Date(long)
        val format = SimpleDateFormat(ANDROID_LOG_TIME_FORMAT, Locale.getDefault())
        return format.format(date)
    }

}

fun Context.createFile(fileName: String): File {
    val path = createPath()
    val listFiles = path.listFiles { _, name -> !name.equals(fileName, true) }
    if (!listFiles.isNullOrEmpty()) {
        listFiles.forEach { it.delete() }
    }
    val file = File(path, fileName)
    return file
}

fun Context.createPath(): File {
    val path = File(filesDir, FileLogTree.LOGS_DIR)
    if (!path.exists()) {
        if (path.mkdirs()) {
            Jivo.i("Create directory for logs")
        }
    }

    return path
}

fun createFileName() = "log_" + SimpleDateFormat(ANDROID_LOG_FILE_FORMAT, Locale.getDefault()).format(Date()) + ".txt"

fun convertPriorityToTag(priority: Int): String {
    return when (priority) {
        Log.INFO -> "I"
        Log.ERROR -> "E"
        else -> "V"
    }

}
