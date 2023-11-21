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

    // TODO Update event.favorites to true
    @Query("UPDATE Event SET isfavorite = 1 WHERE id IN (:ids)")
    fun updateFavorites(ids: List<Int>)

    // TODO Update event.favorite to false
    @Query("UPDATE Event SET isfavorite = 0 WHERE id = :id")
    fun updateUnFavorite(id: Int)

    @Query("SELECT * FROM event ORDER BY dateTime ASC")
    suspend fun getAllEvents(): List<Event>

    // get next id
    @Query("SELECT MAX(id) FROM Event")
    fun getNextId(): Int

    @Query("SELECT * FROM event WHERE :today <= dateTime ORDER BY dateTime ASC")
    suspend fun getFeatureEvents(today: Int): List<Event>

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getEventById(id: Int): Event?

    @Query("SELECT * FROM Event WHERE isfavorite = 1")
    fun getFavoriteEvents(): List<Event>

    // get timestamp less than int
    @Query("SELECT * FROM Event WHERE :today <= dateTime AND dateTime < :timestamp ORDER BY dateTime ASC")
    fun getEventByTime(today: Int, timestamp: Int): List<Event>

    // get timestamp less than int and favorite is 1
    @Query("SELECT * FROM Event WHERE :today <= dateTime AND dateTime < :timestamp AND isfavorite = 1 ORDER BY dateTime ASC")
    fun getFavoriteEventByTime(today: Int, timestamp: Int): List<Event>

    // Set All favorite to 0
    @Query("UPDATE Event SET isfavorite = 0")
    fun clearResetFavorite()

    // Clear all events from the database
    @Query("DELETE FROM Event")
    fun clearAllEvents()

}
