package com.affan.androidfund_dicoding_fp.api


import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUsers(@Query("q") username: String): Call<UserResponse>

    @GET("users")
    fun getAllUser():Call<List<ItemsItem>>


}