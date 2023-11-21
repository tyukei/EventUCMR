package com.kk.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val uid: Int,
    val eid: Int
)