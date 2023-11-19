package com.kk.data

import android.util.Log

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
            return "20$year-$month-$day $hour:00"
        }


    }
}