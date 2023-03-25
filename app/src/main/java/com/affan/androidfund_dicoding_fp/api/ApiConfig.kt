package com.affan.androidfund_dicoding_fp.api


import com.affan.androidfund_dicoding_fp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService():ApiService{
            val loggingInterceptor = Interceptor {
                val req = it.request()
                val reqHeaders = req.newBuilder().addHeader("Authorization", BuildConfig.KEY).build()
                it.proceed(reqHeaders)
            }
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}