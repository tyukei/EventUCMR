package com.kk.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    // その他の設定
}
