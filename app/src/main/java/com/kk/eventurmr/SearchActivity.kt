package com.kk.eventurmr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.core.view.MotionEventCompat
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.FileUtil
import com.kk.data.TimeUtil
import com.kk.eventurmr.list.EventAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : BaseActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchResultsListView: ListView
    private lateinit var adapter: EventAdapter
    private val TAG = "SearchActivity"
    private var lastaction = 0

    // Original full list of items
    private val allItems = mutableListOf<String>()
    private var allEvents = listOf<Event>()

    // List for filtered items
    private val filteredItems = ArrayList<String>()
    private val filteredEvent = ArrayList<Event>()

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileUtil.writeFileStartView(this, TAG)
        setContentView(R.layout.activity_search)
        searchEditText = findViewById(R.id.searchEditText)
        searchResultsListView = findViewById(R.id.searchResultsListView)
        initlist()
        setupMenuBar()
        highlightSelectedIcon(R.id.searchImageView)
        setupSearchEditText()
        setupSearchResultsListView()
        FileUtil.writeFileFinishView(this, TAG)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        //https://yusuke-hata.hatenablog.com/entry/2014/12/05/234032
        val action: Int = MotionEventCompat.getActionMasked(event)
        if(lastaction == action){
            return super.dispatchTouchEvent(event)
        }else {
            lastaction = action
            return when (action) {
                MotionEvent.ACTION_DOWN -> {
                    FileUtil.writeFile(applicationContext,TAG,"ACTION_DOWN","")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_DOWN")
                    super.dispatchTouchEvent(event)
                }

                MotionEvent.ACTION_UP -> {
                    FileUtil.writeFile(applicationContext,TAG,"ACTION_UP","")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_UP")
                    super.dispatchTouchEvent(event)
                }

                MotionEvent.ACTION_MOVE -> {
                    FileUtil.writeFile(applicationContext,TAG,"ACTION_MOVE","")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_MOVE")
                    super.dispatchTouchEvent(event)
                }

                MotionEvent.ACTION_CANCEL -> {
                    FileUtil.writeFile(applicationContext,TAG,"ACTION_CANCEL","")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_CANCEL")
                    super.dispatchTouchEvent(event)
                }

                else -> super.dispatchTouchEvent(event)
            }
        }
    }

    private fun initlist() {
        CoroutineScope(Dispatchers.IO).launch {
            val current = TimeUtil.getCurrentInt()
            allEvents = db.eventDao().getFeatureEvents(current)
            withContext(Dispatchers.Main) {
                filteredEvent.addAll(allEvents)
                adapter.notifyDataSetChanged()
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


    private fun setupSearchResultsListView() {
        adapter = EventAdapter(this@SearchActivity, filteredEvent,db)
        searchResultsListView.adapter = adapter
    }

    private fun performSearch(query: String) {
        filteredEvent.clear()
        if (query.isEmpty()) {
            filteredEvent.addAll(allEvents)
        } else {
            filteredEvent.addAll(allEvents.filter { it.name.contains(query, ignoreCase = true) })
        }
        adapter.notifyDataSetChanged()
    }

}