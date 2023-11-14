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
    @Query("UPDATE Event SET isfavorite = 1 WHERE id = :id")
    fun updateFavorite(id: Int)

    // TODO Update event.favorite to false
    @Query("UPDATE Event SET isfavorite = 0 WHERE id = :id")
    fun updateUnFavorite(id: Int)

    @Query("SELECT * FROM event")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getEventById(id: Int): Event?

    @Query("SELECT * FROM Event WHERE isfavorite = 1")
    fun getFavoriteEvents(): List<Event>

    // get timestamp less than int
    @Query("SELECT * FROM Event WHERE :today <= dateTime AND dateTime < :timestamp")
    fun getEventByTime(today: Int, timestamp: Int): List<Event>

    // Clear all events from the database
    @Query("DELETE FROM Event")
    fun clearAllEvents()

}
