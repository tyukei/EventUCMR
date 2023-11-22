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
import com.kk.data.FileUtil
import com.kk.eventurmr.list.EventAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FavoriteActivity : BaseActivity() {

    private lateinit var favoritesListView: ListView
    private val TAG = "FavoriteActivity"

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileUtil.writeFileStartView(this, TAG)
        setContentView(R.layout.activity_favorite)
        initializeViews()
        setupFavoritesListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.favoriteImageView)
        getFavoriteEvent()
        FileUtil.writeFileFinishView(this, TAG)
    }


    private fun initializeViews() {
        favoritesListView = findViewById(R.id.searchResultsListView)
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
        val adapter = EventAdapter(this@FavoriteActivity, favoriteEvents, db)
        favoritesListView.adapter = adapter

        favoritesListView.setOnItemClickListener { _, _, position, _ ->
            Log.d(TAG, "Item clicked: ${favoriteEvents[position].name}")
            FileUtil.writeFile(
                applicationContext,
                TAG, "TAP",
                "Tap:${favoriteEvents[position].name}"
            )
            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            intent.putExtra("eventId", favoriteEvents[position].id)
            startActivity(intent)
        }
    }


}
