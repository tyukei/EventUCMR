package com.kk.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvents(events: List<Event>)

    // TODO Update event.favorite to true
    @Query("UPDATE Event SET isfavorite = 'true' WHERE id = :id")
    fun updateFavorite(id: Int)

    // TODO Update event.favorite to false
    @Query("UPDATE Event SET isfavorite = 'false' WHERE id = :id")
    fun updateUnFavorite(id: Int)


    @Query("SELECT * FROM event")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getEventById(id: Int): Event?

    @Query("SELECT * FROM Event WHERE isfavorite = 'true'")
    fun getFavoriteEvents(): List<Event>

}
