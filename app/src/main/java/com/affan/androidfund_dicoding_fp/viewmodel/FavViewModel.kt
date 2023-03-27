package com.affan.androidfund_dicoding_fp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.affan.androidfund_dicoding_fp.database.Favorite
import com.affan.androidfund_dicoding_fp.repository.FavoriteRepository

class FavViewModel(application: Application):ViewModel() {
    private val mFavoriteRepository:FavoriteRepository = FavoriteRepository(application)
   fun getAll():LiveData<List<Favorite>> = mFavoriteRepository.getAll()

}