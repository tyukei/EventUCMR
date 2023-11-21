package com.kk.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Event::class, User::class, Favorite::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
}