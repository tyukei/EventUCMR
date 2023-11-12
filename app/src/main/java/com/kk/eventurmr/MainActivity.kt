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
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Use the correct layout file name here
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
        setupListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
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

    private fun addEventToDatabase() {
        val newEvent = Event(
            id = 1,
            name = "Sample Event",
            location = "Sample Location",
            dateTime = "2023-01-01 10:00",
            description = "This is a sample event.",
            isfavorite = false
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                db.eventDao().insertEvent(newEvent)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding event", e)
            }
        }
    }

}