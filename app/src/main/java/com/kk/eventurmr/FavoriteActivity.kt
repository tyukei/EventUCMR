package com.kk.eventurmr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.MotionEventCompat
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
    private var lastaction = 0

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
                TAG, "TAP_LIST",
                "${favoriteEvents[position].name}"
            )
            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            intent.putExtra("eventId", favoriteEvents[position].id)
            startActivity(intent)
        }
    }


}
