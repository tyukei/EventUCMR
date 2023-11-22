package com.kk.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    // drop favorite
    @Query("DELETE FROM Favorite WHERE uid = :uid AND eid = :eid")
    fun dropFavorite(uid: Int, eid: Int)

    // next id
    @Query("SELECT MAX(id) FROM Favorite")
    fun getNextId(): Int

    @Query("SELECT eid FROM Favorite WHERE uid = :uid")
    fun getEidByUid(uid: Int): List<Int>
}
