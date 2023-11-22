package com.kk.data

import android.content.Context
import android.util.Log
import java.io.IOException
import java.time.LocalDateTime

class FileUtil {

    companion object {
        private val TAG = "FileUtil"
        private var timeStartView = System.currentTimeMillis()
        private var timeFinishView = System.currentTimeMillis()
        fun writeFile(context: Context, tag: String, action: String, data: String) {
            try {
                val currentTime = LocalDateTime.now()
                val fos = context.openFileOutput("tap_log.txt", Context.MODE_APPEND)
                val log = "$currentTime,$tag,$action,$data"
                Log.d(TAG, log)
                fos.write(log.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun writeFileStartView(context: Context, tag: String) {
            try {
                timeStartView = System.currentTimeMillis()
                val currentTime = LocalDateTime.now()
                val fos = context.openFileOutput("tap_log.txt", Context.MODE_APPEND)
                val log = "$currentTime,$tag,START_SHOWING_VIEW,"
                Log.d(TAG, log)
                fos.write(log.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun writeFileFinishView(context: Context, tag: String) {
            try {
                timeFinishView = System.currentTimeMillis()
                val currentTime = LocalDateTime.now()
                val fos = context.openFileOutput("tap_log.txt", Context.MODE_APPEND)
                val log = "$currentTime,$tag,FINISH_SHOWING_VIEW,${timeFinishView - timeStartView}"
                Log.d(TAG, log)
                fos.write(log.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
