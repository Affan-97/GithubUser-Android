package com.affan.androidfund_dicoding_fp.helper

import androidx.appcompat.app.AppCompatDelegate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ThemeHelperTest {
    private lateinit var themeHelper: ThemeHelper

    @Before
    fun setUp() {
        themeHelper = ThemeHelper()
    }

    @Test
    fun testSwitchTheme() {
        val isChecked = false
        val expectedIsNight = true
        val expectedMode = AppCompatDelegate.MODE_NIGHT_YES
            themeHelper.switchTheme(isChecked)

        val actualIsNight =  ThemeHelper.isNight

        val actualMode = AppCompatDelegate.getDefaultNightMode()

        assertEquals(expectedIsNight,actualIsNight)
        assertEquals(expectedMode, actualMode)
    }
}