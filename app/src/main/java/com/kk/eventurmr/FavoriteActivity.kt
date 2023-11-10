package com.kk.eventurmr

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FavoriteActivity : BaseActivity() {

    private lateinit var favoritesListView: ListView
    private lateinit var favoriteTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite) // Use the correct layout resource name

        initializeViews()
        setupFavoritesListView()
        setupMenuBar()
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
        // Dummy data for the list view
        val favoriteItems = listOf("Favorite 1", "Favorite 2", "Favorite 3")

        // Using a simple list item layout provided by Android and a basic ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteItems)
        favoritesListView.adapter = adapter

        // Set an item click listener for the ListView
        favoritesListView.setOnItemClickListener { _, _, position, _ ->
            // Handle the list item click action here
            val selectedItem = favoriteItems[position]
            // Do something with the selected item, e.g., navigate to a detail view
        }
    }
}
