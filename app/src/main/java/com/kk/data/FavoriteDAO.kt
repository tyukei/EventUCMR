package com.kk.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    // next id
    @Query("SELECT MAX(id) FROM Favorite")
    fun getNextId(): Int

    @Query("SELECT eid FROM Favorite WHERE uid = :uid")
    fun getEidByUid(uid: Int): List<Int>
}
