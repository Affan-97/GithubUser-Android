package com.affan.androidfund_dicoding_fp.ui

import android.content.Context
import android.content.Intent
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import com.affan.androidfund_dicoding_fp.ui.DetailActivity.Companion.USERNAME
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers.*

import com.affan.androidfund_dicoding_fp.R
import com.affan.androidfund_dicoding_fp.database.FavRoomDatabase
import com.affan.androidfund_dicoding_fp.database.Favorite


class FavoriteActivityTest {
    companion object {
        val dummyUser = "Testdata"
        val dummyURL = "http://dummyurl.com"
    }

    private lateinit var activity: DetailActivity

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtra(USERNAME, dummyUser)
        }
        ActivityScenario.launch<DetailActivity>(intent).onActivity { activity = it }
    }

    @Test
    fun testActionBarTitle() {
        val expectedTitle = "Detail $dummyUser"

        assertEquals(expectedTitle, activity.supportActionBar?.title)
    }

    @Test
    fun checkIfDetailActivityIsDisplayed() {
        onView(withId(R.id.detail_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfNameTextViewIsDisplayed() {
        onView(withId(R.id.tv_name)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfUsernameTextViewIsDisplayed() {
        onView(withId(R.id.tv_username)).check(matches(isDisplayed()))
    }


    @Test
    fun checkIfFabAddButtonIsDisplayed() {
        onView(withId(R.id.fab_add)).check(matches(isDisplayed()))
    }

    @Test

    fun testData() {
        val db = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FavRoomDatabase::class.java, "test-database"
        ).build()
        onView(withId(R.id.fab_add)).perform(click())


        val favorite = Favorite(dummyURL, dummyURL)
        db.favDao().insert(favorite)


        val savedFavorite = db.favDao().isSaved(dummyURL, dummyURL)
        assertTrue(savedFavorite)

        onView(withId(R.id.fab_add)).perform(click())


    }


}