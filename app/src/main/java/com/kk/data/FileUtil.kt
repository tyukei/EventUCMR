package com.kk.data

import android.content.Context
import android.util.Log
import java.io.IOException
import java.time.LocalDateTime

class FileUtil {

    companion object {
        private val FILENAME = "android-log.csv"
        private val TAG = "FileUtil"
        private var timeStartView = System.currentTimeMillis()
        private var timeFinishView = System.currentTimeMillis()
        private var timeFinishActivity = System.currentTimeMillis()
        private var lastFavorite = System.currentTimeMillis()
        fun writeFile(context: Context, tag: String, action: String, data: String) {
            try {
                val currentTime = LocalDateTime.now()
                val fos = context.openFileOutput(FILENAME, Context.MODE_APPEND)
                val log = "$currentTime,$tag,$action,$data \n"
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
                val fos = context.openFileOutput(FILENAME, Context.MODE_APPEND)
                val log = "$currentTime,$tag,START_SHOWING_VIEW, \n"
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
                val fos = context.openFileOutput(FILENAME, Context.MODE_APPEND)
                val log =
                    "$currentTime,$tag,FINISH_SHOWING_VIEW,\n$currentTime,$tag,TIME_SHOWING,${(timeFinishView - timeStartView)}\n"
                Log.d(TAG, log)
                fos.write(log.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun writeFileFinishActivity(context: Context, tag: String,destination:String) {
            try {
                timeFinishActivity = System.currentTimeMillis()
                val currentTime = LocalDateTime.now()
                val fos = context.openFileOutput(FILENAME, Context.MODE_APPEND)
                val log =
                    "$currentTime,$tag,FINISH_ACTIVITY,$destination\n$currentTime,$tag,TIME_ACTIVITY,${(timeFinishActivity - timeStartView)}\n"
                Log.d(TAG, log)
                fos.write(log.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }

        fun writeFileFavorite(context: Context, tag: String, eventName: String) {
            try {
                val currentTime = LocalDateTime.now()
                val currentFavorite = System.currentTimeMillis()
                val fos = context.openFileOutput(FILENAME, Context.MODE_APPEND)
                val log =
                    "$currentTime,$tag,TAP_FAVORITE,$eventName\n$currentTime,$tag,TIME_FAVORITE,${currentFavorite - lastFavorite}\n"
                lastFavorite = currentFavorite
                Log.d(TAG, log)
                fos.write(log.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun writeFileUnFavorite(context: Context, tag: String, eventName: String) {
            try {
                val currentTime = LocalDateTime.now()
                val currentFavorite = System.currentTimeMillis()
                val fos = context.openFileOutput(FILENAME, Context.MODE_APPEND)
                val log = "$currentTime,$tag,TAP_UNFAVORITE,$eventName\n"
                Log.d(TAG, log)
                fos.write(log.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
