package com.kk.eventurmr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : BaseActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchResultsListView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    // Original full list of items
    private val allItems = mutableListOf<String>()
    private var allEvents = listOf<Event>()

    // List for filtered items
    private val filteredItems = ArrayList<String>()

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        searchResultsListView = findViewById(R.id.searchResultsListView)
        initlist()
        setupMenuBar()
        highlightSelectedIcon(R.id.searchImageView)
    }

    private fun initlist() {
        CoroutineScope(Dispatchers.IO).launch {
            allEvents = db.eventDao().getAllEvents()
            val eventNames = allEvents.map { it.name }
            withContext(Dispatchers.Main) {
                allItems.clear()
                allItems.addAll(eventNames)
                filteredItems.addAll(allItems)
                setupSearchResultsListView()
                setupSearchEditText()
            }
        }
    }


    private fun setupSearchEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                performSearch(s.toString().trim())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun performSearch(query: String) {
        filteredItems.clear()
        if (query.isEmpty()) {
            filteredItems.addAll(allItems)
        } else {
            val searchResults = allItems.filter { it.startsWith(query, ignoreCase = true) }
            filteredItems.addAll(searchResults)
        }
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchResultsListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredItems)
        searchResultsListView.adapter = adapter
    }
}