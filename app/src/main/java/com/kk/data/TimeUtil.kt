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


    }
}