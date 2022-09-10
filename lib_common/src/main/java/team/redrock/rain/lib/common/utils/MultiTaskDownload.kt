package team.redrock.rain.lib.common.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.internal.headersContentLength
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import kotlin.coroutines.suspendCoroutine

private val okhttpClient = OkHttpClient.Builder().build()

private fun Long.splitRanges(size: Int): List<LongRange> {
    val list = mutableListOf<LongRange>()
    var current = this
    var cursor = 0L
    while (current / size > 0) {
        list.add(LongRange(cursor, cursor + size))
        cursor += size
        current -= size
    }
    list.add(LongRange(cursor, cursor + current))
    return list
}

suspend fun File.downloadMultiTask(url: String, singleSize: Int) = withContext(Dispatchers.IO) {
    val response = request(url)
    @Suppress("BlockingMethodInNonBlockingContext")
    val file = RandomAccessFile(this@downloadMultiTask, "rw")
    val length = response.headersContentLength()
    length.splitRanges(singleSize)
        .map { it to request(url, it) }
        .map { (range, resp) ->
            async {
                @Suppress("BlockingMethodInNonBlockingContext")
                file.seek(range.first)
                val bytes = ByteArray(1024)
                var len: Int
                resp.body!!.byteStream().use { input ->
                    @Suppress("BlockingMethodInNonBlockingContext")
                    while (input.read(bytes).also { len = it } >= 0) {
                        file.write(bytes, 0, len)
                    }
                }
                @Suppress("BlockingMethodInNonBlockingContext")
                file.close()
            }
        }.forEach { it.await() }
}

suspend fun request(url: String, range: LongRange? = null) = suspendCoroutine<Response> {
    okhttpClient.newCall(
        Request.Builder()
            .url(url)
            .apply {
                range?.let { addHeader("Range", "bytes=${range.first}-${range.last}") }
            }
            .build()
    ).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            it.resumeWith(Result.failure(e))
        }

        override fun onResponse(call: Call, response: Response) {
            it.resumeWith(Result.success(response))
        }
    })
}