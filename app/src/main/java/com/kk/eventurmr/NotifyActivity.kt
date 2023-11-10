package com.kk.eventurmr

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NotifyActivity : BaseActivity() {

    private lateinit var upcomingEventsListView: ListView
    private lateinit var upcomingEventTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify) // make sure to use the correct layout file name

        initializeViews()
        populateListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.notificationImageView)
    }

    private fun initializeViews() {
        upcomingEventTextView = findViewById(R.id.favoriteTextView) // If this TextView is not for favorites, consider renaming the ID in your XML and here in Kotlin
        upcomingEventsListView = findViewById(R.id.searchResultsListView)

        // Configure the TextView if needed
        // For example, you might want to set an OnClickListener to perform an action when the text is clicked
    }

    private fun populateListView() {
        // Placeholder data for the ListView
        val events = arrayOf("Event 1", "Event 2", "Event 3") // Replace with your actual event data

        // Create an ArrayAdapter using the default list item layout and the events data
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, events)
        upcomingEventsListView.adapter = adapter

        // Set up the item click listener for the ListView
        upcomingEventsListView.setOnItemClickListener { _, _, position, _ ->
            // You can handle the event click here
            // For example, you might want to open a new Activity with the event details
        }
    }
}
