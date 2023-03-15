package com.affan.androidfund_dicoding_fp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.affan.androidfund_dicoding_fp.api.ApiConfig
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel:ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>?>()
    val listUser: LiveData<List<ItemsItem>?> = _listUser

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading
    companion object{
        private const val TAG ="MainViewModel"
    }
    init {
        getData()
    }

    fun getData() {
        _loading.value = true
        val client = ApiConfig.getApiService().getAllUser()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful){
                    _loading.value = false
                    val responseBody = response.body()
                    if (responseBody!=null){
                        _listUser.value = responseBody
                    }
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findUser(name:String) {
        _loading.value = true
        val client = ApiConfig.getApiService().searchUsers(name)
        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful){
                    _loading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = responseBody.items
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}