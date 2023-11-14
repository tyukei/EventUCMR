package com.kk.eventurmr

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : BaseActivity() {
    private val TAG = "DetailActivity"
    private lateinit var titleTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var dateTimeTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var favriteButton: ImageButton

    private var eventId: Int = -1
    private var isfavorite: Boolean = false

    // Assuming you have a singleton pattern for the database, else initialize it here
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        titleTextView = findViewById(R.id.eventDetailTitle)
        locationTextView = findViewById(R.id.locationTextView)
        dateTimeTextView = findViewById(R.id.dateTimeTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        favriteButton = findViewById(R.id.favoriteImageButton)
        favriteButton.setOnClickListener {
            onClickFavorite()
        }
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
        eventId = intent.getIntExtra("eventId", -1)
        CoroutineScope(Dispatchers.IO).launch {
            val event = db.eventDao().getEventById(eventId)
            Log.d(TAG, "event:$event")
            event?.let {
                withContext(Dispatchers.Main) {
                    titleTextView.text = it.name
                    locationTextView.text = it.location
                    dateTimeTextView.text = it.dateTime.toString()
                    descriptionTextView.text = it.description
                    isfavorite = it.isfavorite
                    Log.d(TAG, "heart:$isfavorite")
                    setupHeartIcon()
                    Log.d(TAG, "Event: $it")
                }
            }
        }
    }


    private fun onClickFavorite() {
        // TODO Update the event.favorite to true

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (isfavorite) {
                    db.eventDao().updateUnFavorite(eventId)
                    isfavorite = false
                    favriteButton.setBackgroundResource(R.drawable.ic_favorite)
                    var event = db.eventDao().getEventById(eventId)
                    Log.d(TAG, "event:$event")
                } else {
                    db.eventDao().updateFavorite(eventId)
                    isfavorite = true
                    favriteButton.setBackgroundResource(R.drawable.ic_favorite_selected)
                    var event = db.eventDao().getEventById(eventId)
                    Log.d(TAG, "event:$event")
                }
                setupHeartIcon()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding event", e)
            }
        }
    }

    private fun setupHeartIcon() {
        if (isfavorite) {
            Log.d(TAG, "selected heart")
            favriteButton.setBackgroundResource(R.drawable.ic_favorite_selected)
        } else {
            Log.d(TAG, "unselected heart")
            favriteButton.setBackgroundResource(R.drawable.ic_favorite)
        }
    }
}
