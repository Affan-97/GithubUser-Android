package com.affan.androidfund_dicoding_fp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.affan.androidfund_dicoding_fp.api.ApiConfig
import com.affan.androidfund_dicoding_fp.api.Detail
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>?>()
    val listUser: LiveData<List<ItemsItem>?> = _listUser

    private var _listFollowers = MutableLiveData<List<ItemsItem>?>()
    val listFollowers: LiveData<List<ItemsItem>?> = _listFollowers

    private var _listFollowing = MutableLiveData<List<ItemsItem>?>()
    val listFollowing: LiveData<List<ItemsItem>?> = _listFollowing

    private val _detailUser = MutableLiveData<Detail?>()
    val detailUser: LiveData<Detail?> = _detailUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _textError = MutableLiveData<String>()
    val textError: LiveData<String> = _textError



    init {
        getData()
    }

    fun getData() {
        _loading.value = true
        val client = ApiConfig.getApiService().getAllUser()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _error.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = responseBody
                    }
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loading.value = false
                _error.value = true
                _textError.value = t.message
            }
        })
    }

    fun getUser(username: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<Detail> {
            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _error.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody
                    }
                }
            }
            override fun onFailure(call: Call<Detail>, t: Throwable) {
                _loading.value = false
                _error.value = true
                _textError.value = t.message
            }
        })

    }

    fun findUser(name: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().searchUsers(name)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _error.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = responseBody.items
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _loading.value = false
                _error.value = true
                _textError.value = t.message
            }
        })
    }

    fun getFollowers(username: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _error.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = responseBody
                    }
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loading.value = false
                _error.value = true
                _textError.value = t.message
            }
        })
    }

    fun getFollowing(username: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _error.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = responseBody

                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loading.value = false
                _error.value = true
                _textError.value = t.message

            }
        })
    }
}