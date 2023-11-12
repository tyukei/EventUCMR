package com.kk.eventurmr

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.kk.data.AppDatabase
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var dateTimeTextView: TextView
    private lateinit var descriptionTextView: TextView

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

        val eventId = intent.getIntExtra("eventId", -1)

        CoroutineScope(Dispatchers.IO).launch {
            val event = db.eventDao().getEventById(eventId)
            event?.let {
                withContext(Dispatchers.Main) {
                    titleTextView.text = it.name
                    locationTextView.text = it.location
                    dateTimeTextView.text = it.dateTime
                    descriptionTextView.text = it.description
                }
            }
        }
    }
}
