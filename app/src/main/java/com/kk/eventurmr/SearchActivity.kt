package com.kk.eventurmr

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : BaseActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchResultsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search) // Replace with your actual layout resource name

        searchEditText = findViewById(R.id.searchEditText)
        searchResultsListView = findViewById(R.id.searchResultsListView)

        setupSearchResultsListView()
        setupSearchEditText()
        setupMenuBar()
        highlightSelectedIcon(R.id.searchImageView)
    }

    private fun setupSearchEditText() {
        searchEditText.setOnEditorActionListener { textView, i, keyEvent ->
            // Handle search action here
            // For example, query a database or a server with textView.text.toString()
            true // Return true to indicate you have handled the event
        }
    }

    private fun setupSearchResultsListView() {
        // Placeholder data for the ListView
        val searchResults = arrayOf("Result 1", "Result 2", "Result 3") // Replace with real search results

        // Create an ArrayAdapter using the default list item layout and the search results
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchResults)
        searchResultsListView.adapter = adapter

        // Set up the item click listener for the ListView
        searchResultsListView.setOnItemClickListener { _, _, position, _ ->
            // You can handle the search result click here
            // For example, you might want to open a new Activity with the result details
        }
    }
}
