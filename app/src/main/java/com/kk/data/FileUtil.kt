package com.kk.data

import android.content.Context
import android.util.Log
import java.io.IOException
import java.time.LocalDateTime

class FileUtil {

    companion object {
        private val TAG = "FileUtil"
        fun writeFile(context: Context, data: String) {
            Log.d(TAG, "writeFile: $data")
            try {
                val currentTime = LocalDateTime.now()
                val fos = context.openFileOutput("tap_log.txt", Context.MODE_APPEND)
                fos.write(
                    "$currentTime,$data".toByteArray()
                )
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
