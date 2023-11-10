package com.kk.eventurmr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"
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

        // Set an item click listener to open DetailActivity with the event's details
        eventsListView.setOnItemClickListener { _, _, position, _ ->
            // Create an intent to start DetailActivity
            val intent = Intent(this, DetailActivity::class.java)
            // (Optional) Pass data to DetailActivity. For example, pass the event name:
            Log.d(TAG, "Event name: ${events[position]}")
            intent.putExtra("eventName", events[position])
            startActivity(intent)
        }
    }
}
