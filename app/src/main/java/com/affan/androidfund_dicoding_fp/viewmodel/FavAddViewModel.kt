package com.affan.androidfund_dicoding_fp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.affan.androidfund_dicoding_fp.database.Favorite
import com.affan.androidfund_dicoding_fp.repository.FavoriteRepository

class FavAddViewModel(application: Application) : ViewModel() {
    private val mFavRepos: FavoriteRepository = FavoriteRepository(application)
    fun insert(fav: Favorite) {
        mFavRepos.insert(fav)
    }

    fun delete(fav: Favorite) {
        mFavRepos.delete(fav)
    }
    fun isSaved(username: String, avatarUrl: String, callback: (Boolean) -> Unit) {
        mFavRepos.isSaved(username, avatarUrl) { result ->
            callback(result)
        }
    }
}