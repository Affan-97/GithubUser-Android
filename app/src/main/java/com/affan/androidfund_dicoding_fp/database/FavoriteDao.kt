package com.affan.androidfund_dicoding_fp.database

import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: Favorite)

    @Delete
    fun delete(fav: Favorite)

    @Query("SELECT * from favorite")
    fun getAll(): LiveData<List<Favorite>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE username = :username AND avatarUrl = :avatarUrl)")
    fun isSaved(username: String,avatarUrl:String): Boolean
}