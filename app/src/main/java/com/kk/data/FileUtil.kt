package com.kk.data

import android.content.Context
import android.util.Log
import java.io.IOException
import java.time.LocalDateTime

class FileUtil {

    companion object {
        private val TAG = "FileUtil"
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
    }
}
