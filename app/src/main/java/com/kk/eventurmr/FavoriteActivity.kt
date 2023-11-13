package com.kk.eventurmr

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteActivity : BaseActivity() {

    private lateinit var favoritesListView: ListView
    private lateinit var favoriteTextView: TextView


    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite) // Use the correct layout resource name

        initializeViews()
        setupFavoritesListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.favoriteImageView)
    }

    private fun initializeViews() {
        favoriteTextView = findViewById(R.id.favoriteTextView)
        favoritesListView = findViewById(R.id.searchResultsListView)

        // Setting up the favorite text view click listener if needed
        favoriteTextView.setOnClickListener {
            // Handle the bookmark/favorite click action here
        }
    }

    private fun setupFavoritesListView() {
        // Initial dummy data for the list view
        val favoriteItems = mutableListOf("Favorite 1", "Favorite 2", "Favorite 3")

        // Adapter setup
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteItems)
        favoritesListView.adapter = adapter

        // Item click listener
        favoritesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = favoriteItems[position]
            // Handle the list item click, e.g., navigate to a detail view
        }
    }

    private fun getFavoriteEvent() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val favoriteEvent = db.eventDao().getFavoriteEvents()
                // Switch to the main thread to update UI
                withContext(Dispatchers.Main) {
                    updateListView(favoriteEvent)
                }
            } catch (e: Exception) {
                // Log the error or update the UI accordingly
                withContext(Dispatchers.Main) {
                    // Show error message to the user
                }
            }
        }
    }

    private fun updateListView(favoriteEvents: List<Event>) {
        val adapter = favoritesListView.adapter as ArrayAdapter<String>

        // Convert each Event object to a String
        val eventStrings = favoriteEvents.map { event ->
            // Assuming Event class has a 'name' property. Modify as per your Event class structure.
            event.name
        }

        adapter.clear()
        adapter.addAll(eventStrings)
        adapter.notifyDataSetChanged()
    }


}
