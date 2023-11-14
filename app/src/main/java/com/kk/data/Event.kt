package com.kk.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val location: String,
    val dateTime: Int,
    val description: String,
    var isfavorite: Boolean
)