package com.kk.eventurmr

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : BaseActivity() {

    private lateinit var eventsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Use the correct layout file name here
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
        setupListView()

    }

    private fun setupListView() {
        eventsListView = findViewById(R.id.eventsListView)

        // Dummy data for the list
        val events = arrayOf("Event 1", "Event 2", "Event 3")

        // Using a simple list item layout provided by Android and a basic ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, events)
        eventsListView.adapter = adapter

        // Set an item click listener, if needed
        eventsListView.setOnItemClickListener { parent, view, position, id ->
            // Handle the list item click, e.g., open a new activity with event details
        }
    }
}
