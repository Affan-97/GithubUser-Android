package com.affan.androidfund_dicoding_fp.helper

import androidx.appcompat.app.AppCompatDelegate

class ThemeHelper() {
companion object{
   var isNight = false
}
fun switchTheme(isChecked: Boolean){
    isNight = if (!isChecked) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        true
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        false
    }
}
}