package com.kk.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Event::class, User::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
}