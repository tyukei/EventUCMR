package com.kk.eventurmr

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.TimeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotifyActivity : BaseActivity() {

    private lateinit var notifyListView: ListView

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify) // make sure to use the correct layout file name
        initializeViews()
        setuptNotifyListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.notificationImageView)
        getNotifyEvent()
    }

    private fun initializeViews() {
        notifyListView = findViewById(R.id.notifyListView)
    }

    private fun setuptNotifyListView() {
        // Initial dummy data for the list view
        val notifyItems = mutableListOf("Favorite 1", "Favorite 2", "Favorite 3")

        // Adapter setup
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notifyItems)
        notifyListView.adapter = adapter

        // Item click listener
        notifyListView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = notifyItems[position]
            // Handle the list item click, e.g., navigate to a detail view
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotifyEvent() {
        // get today like 2023-10-10-10:10
        val today = LocalDateTime.now()
        val twoWeek = LocalDateTime.now().plusWeeks(2)
        val formatToday = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val strToday = formatToday.format(today)
        val strFormat = format.format(twoWeek)
        val intToday = TimeUtil.getDateInt(strToday)
        val intFormat = TimeUtil.getDateInt(strFormat)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val within2weekEvent = db.eventDao().getEventByTime(intToday, intFormat)
                withContext(Dispatchers.Main) {
                    updateListView(within2weekEvent)
                }
            } catch (e: Exception) {
                // Log the error or update the UI accordingly
                withContext(Dispatchers.Main) {
                    // Show error message to the user
                }
            }
        }
    }

    private fun updateListView(notifyEvents: List<Event>) {
        val adapter = notifyListView.adapter as ArrayAdapter<String>

        // Convert each Event object to a String
        val eventStrings = notifyEvents.map { event ->
            // Assuming Event class has a 'name' property. Modify as per your Event class structure.
            event.name
        }

        adapter.clear()
        adapter.addAll(eventStrings)
        adapter.notifyDataSetChanged()
    }


}
