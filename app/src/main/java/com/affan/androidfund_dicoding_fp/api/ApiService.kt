package com.affan.androidfund_dicoding_fp.api


import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUsers(@Query("q") username: String): Call<UserResponse>

    @GET("users")
    fun getAllUser():Call<List<ItemsItem>>

    @GET("users/{username}/followers")
    fun getFollowers( @Path("username") username: String):Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing( @Path("username") username: String):Call<List<ItemsItem>>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username:String
    ): Call<Detail>

}