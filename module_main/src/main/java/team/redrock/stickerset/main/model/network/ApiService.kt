package team.redrock.stickerset.main.model.network

import okhttp3.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import team.redrock.stickerset.main.model.data.BaseResponse
import team.redrock.stickerset.main.model.data.StickerSet
import team.redrock.stickerset.main.model.data.TeleFile
import java.io.IOException
import kotlin.coroutines.suspendCoroutine

interface ApiService {
    @GET("/bot{token}/getStickerSet")
    suspend fun getStickerSet(@Path("token") token: String, @Query("name") name: String): BaseResponse<StickerSet>

    @GET("/bot{token}/getFile")
    suspend fun getFile(@Path("token") token: String, @Query("file_id") fileId: String): BaseResponse<TeleFile>

    companion object : ApiService by RetrofitHelper.apiService {

        private val okHttpClient = OkHttpClient.Builder().build()

        suspend fun getWebFile(url: String): ByteArray = suspendCoroutine {
            okHttpClient.newCall(
                Request.Builder()
                    .url(url)
                    .build()
            ).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.resumeWith(Result.failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    it.resumeWith(Result.success(response.body!!.bytes()))
                }
            })
        }
    }
}