package com.affan.androidfund_dicoding_fp.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){
    private val THEME = booleanPreferencesKey("theme")
    fun getTheme(): Flow<Boolean>{
        return dataStore.data.map {
            it[THEME]?:false
        }
    }
    suspend fun saveTheme(isDark:Boolean){
        dataStore.edit {
            it[THEME] = isDark
        }
    }
    companion object{
        @Volatile
        private var INSTANCE :SettingPreferences?=null
        fun getInstance(dataStore: DataStore<Preferences>):SettingPreferences{
            return INSTANCE?: synchronized(this){
                val instance = SettingPreferences(dataStore)
                INSTANCE=instance
                instance
            }
        }
    }
}