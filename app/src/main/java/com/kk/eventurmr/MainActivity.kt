package com.kk.eventurmr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.EventDBUtil
import com.kk.data.FileUtil
import com.kk.data.TimeUtil
import com.kk.eventurmr.list.EventAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"
    private lateinit var eventsListView: ListView

    // Roomデータベースのインスタンスを作成
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        )
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Use the correct layout file name here
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
        setupListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
    }
    private fun setupListView() {
        eventsListView = findViewById(R.id.eventsListView)
        lifecycleScope.launch {
            try {
                val events = getEventsFromDatabase()
                for (event in events) {
                    Log.d(TAG, "Event: ${event.name}")
                }
                withContext(Dispatchers.Main) {
                    val adapter = EventAdapter(this@MainActivity, events,db)
                    eventsListView.adapter = adapter
                    eventsListView.setOnItemClickListener { _, _, position, _ ->
                        Log.d(TAG, "Item clicked: ${events[position].name}")
                        FileUtil.writeFile(
                            applicationContext,
                            "Tap:${events[position].name}"
                        )
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra("eventId", events[position].id)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching events from database", e)
            }
        }
    }


    // データベースからイベントを取得する非同期関数
    private suspend fun getEventsFromDatabase(): List<Event> {
        return withContext(Dispatchers.IO) {
            val current = TimeUtil.getCurrentInt()
            db.eventDao().getFeatureEvents(current)
        }
    }

    // Clear db
    private fun clearAllEvents() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                db.eventDao().clearAllEvents()
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing events from database", e)
            }
        }
    }


}