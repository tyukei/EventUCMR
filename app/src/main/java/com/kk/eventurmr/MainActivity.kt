package com.kk.eventurmr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.TimeUtil
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
        //clearAllEvents()
        addEventToDatabase()
    }

    private fun setupListView() {
        eventsListView = findViewById(R.id.eventsListView)

        lifecycleScope.launch {
            try {
                // データベースからイベントを取得してListViewにセットする
                val events = getEventsFromDatabase()
                val adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    events.map { it.name })
                eventsListView.adapter = adapter

                eventsListView.setOnItemClickListener { _, _, position, _ ->
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("eventId", events[position].id) // イベントのIDを渡す
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching events from database", e)
                // Handle the error appropriately
            }
        }
    }

    // データベースからイベントを取得する非同期関数
    private suspend fun getEventsFromDatabase(): List<Event> {
        return withContext(Dispatchers.IO) {
            db.eventDao().getAllEvents()
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


    private fun addEventToDatabase() {
//        val timestamp1 = TimeUtil.getTimeStamp("2023-11-01 10:00")
//        val timestamp2 = TimeUtil.getTimeStamp("2023-11-02 10:00")
//        val timestamp3 = TimeUtil.getTimeStamp("2023-11-03 10:00")
        val timestamp1 = TimeUtil.getDateInt("2023-11-20 10:00")
        val timestamp2 = TimeUtil.getDateInt("2023-11-30 10:00")
        val timestamp3 = TimeUtil.getDateInt("2023-12-01 10:00")
        val newEvent = Event(
            id = 1,
            name = "Sample Event",
            location = "UCMerced",
            dateTime = timestamp1,
            description = "This is a sample event.",
            isfavorite = false
        )
        val newEvent2 = Event(
            id = 2,
            name = "Sample Event2",
            location = "Tokyo Station",
            dateTime = timestamp2,
            description = "This is a sample event2.",
            isfavorite = false
        )
        val newEvent3 = Event(
            id = 3,
            name = "Sample Event3",
            location = "Oosaka castle",
            dateTime = timestamp3,
            description = "This is a sample event3.",
            isfavorite = false
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val events = listOf(newEvent, newEvent2, newEvent3)
                db.eventDao().insertEvents(events)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding event", e)
            }
        }
    }

}