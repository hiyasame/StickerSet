package team.redrock.stickerset.main.model.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import team.redrock.rain.lib.common.network.ApiGenerator

object RetrofitHelper {
    private val retrofit by lazy { initRetrofit() }

    val apiService by lazy { retrofit.create<ApiService>() }

    private fun initRetrofit(): Retrofit {
        return ApiGenerator.getNewRetrofit(false) {
            client(getClient())
        }
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}