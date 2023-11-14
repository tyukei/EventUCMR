package com.kk.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    companion object {
        fun getTimeStamp(time: String): Timestamp {
            //"2023-11-01 10:00"
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val dateTime = LocalDateTime.parse(time, formatter)
            return Timestamp.valueOf(dateTime.toString())
        }
    }
}