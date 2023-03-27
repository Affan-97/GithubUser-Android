package com.affan.androidfund_dicoding_fp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.affan.androidfund_dicoding_fp.preferences.SettingPreferences
import kotlinx.coroutines.launch

class PrefViewModel(private val preferences: SettingPreferences):ViewModel() {
    fun getTheme():LiveData<Boolean>{
        return preferences.getTheme().asLiveData()
    }
    fun saveTheme(isDark:Boolean){
        viewModelScope.launch {
            preferences.saveTheme(isDark)
        }
    }
}