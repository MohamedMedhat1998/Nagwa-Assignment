package com.mohamed.medhat.nagwaassignment.utils.download

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

private const val TAG = "DownloadManager"

/**
 * Responsible for downloading files from the internet.
 */
class DownloadManager @Inject constructor(@ApplicationContext private val context: Context) {

    var isCancelled = false

    /**
     * Downloads a file from the internet.
     * @param link The link of the file to download.
     * @param fileName The name of the file to download including the file extension.
     * @param onProgress The lambda that gets invoked when the download makes some progress.
     * @return `true` if the download was successful, `false` otherwise.
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun download(link: String, fileName: String, onProgress: suspend (Int) -> Unit): Boolean {
        val input: InputStream?
        val output: OutputStream?
        val connection: HttpURLConnection?
        try {
            val url = URL(link)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(
                    TAG,
                    "download: Download failed: ${connection.responseCode}, ${connection.responseMessage}"
                )
                return false
            }

            val fileLength: Int = connection.contentLength

            input = connection.inputStream
            val file = File("${context.filesDir}/$fileName")
            if (file.exists()) {
                file.delete()
            }
            output = FileOutputStream("${context.filesDir}/$fileName")
            val data = ByteArray(4096)
            var total: Long = 0
            var count: Int
            var previousProgress = 0
            var progress = 0
            while (input.read(data).also { count = it } != -1) {
                if (isCancelled) {
                    input.close()
                }
                total += count.toLong()
                progress = (total * 100 / fileLength).toInt()
                if (fileLength > 0) {
                    if (progress > previousProgress) {
                        onProgress.invoke(progress)
                        previousProgress = progress
                    }
                }
                output.write(data, 0, count)
            }
            output.close()
            input?.close()
            connection.disconnect()
            return !isCancelled
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}