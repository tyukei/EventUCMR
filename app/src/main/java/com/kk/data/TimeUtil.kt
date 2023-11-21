package com.kk.data

import android.util.Log
import java.time.LocalDate
import java.time.LocalTime

class TimeUtil {
    companion object {
//        fun getTimeStamp(time: String): Timestamp {
//            //"2023-11-01 10:00"
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//            val dateTime = LocalDateTime.parse(time, formatter)
//            return Timestamp.valueOf(dateTime.toString())
//        }

        fun getDateInt(time: String): Int {
            //"2023-11-01 10:00" to 202311011000 erase -,: and space
            val timeSimple = time.replace("-", "").replace(" ", "").replace(":", "")
            // 初めの3桁と最後の２桁削除
            val timeMini = timeSimple.substring(2, 10)
            Log.d("TimeUtil", "$timeMini")
            return timeMini.toInt()
        }

        fun getDateString(time: Int?): String {
            // 23110110 to 2023-11-01 10:00
            val timeString = time.toString()
            val year = timeString.substring(0, 2)
            val month = timeString.substring(2, 4)
            val day = timeString.substring(4, 6)
            val hour = timeString.substring(6, 8)
            return if (hour.toInt() < 12) {
                "20$year-$month-$day ($hour:00 am)"
            } else {
                val newhour = hour.toInt() - 12
                "20$year-$month-$day ($newhour:00 pm)"
            }
        }

        fun getCurrentInt(): Int {
            // get 2023-11-01 10:00
            val current = LocalDate.now()
            val year = current.year.toString().substring(2, 4)
            val month = current.monthValue.toString().padStart(2, '0')
            val day = current.dayOfMonth.toString().padStart(2, '0')
            val currentTime = LocalTime.now()
            val hour = currentTime.hour.toString().padStart(2, '0')
            Log.d("TimeUtil", "$year$month$day$hour")
            return ("$year$month$day$hour").toInt()
        }


    }
}