package com.affan.androidfund_dicoding_fp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.affan.androidfund_dicoding_fp.database.FavRoomDatabase
import com.affan.androidfund_dicoding_fp.database.Favorite
import com.affan.androidfund_dicoding_fp.database.FavoriteDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favDao()

    }

    fun getAll(): LiveData<List<Favorite>> = mFavoriteDao.getAll()
    fun insert(fav: Favorite) {
        executorService.execute { mFavoriteDao.insert(fav) }
    }

    fun isSaved(username: String, avatarUrl: String, callback: (Boolean) -> Unit) {
        executorService.execute {
            val result = mFavoriteDao.isSaved(username, avatarUrl)
            callback(result)
        }
    }

    fun delete(fav: Favorite) {
        executorService.execute { mFavoriteDao.delete(fav) }
    }
}