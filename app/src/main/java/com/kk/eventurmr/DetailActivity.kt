package com.kk.eventurmr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.FileUtil
import com.kk.data.TimeUtil
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
    private var locationStr: String = "Kyoto"

    // Assuming you have a singleton pattern for the database, else initialize it here
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileUtil.writeFileStartView(this, TAG)
        setContentView(R.layout.activity_detail)
        initView()
        setupMenuBar()
        setDB()
        locationTextView.setOnClickListener {
//            val uri = Uri.parse("geo:0,0?q=$locationStr")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)

            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(locationStr)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // この行を追加
                applicationContext.startActivity(it)
            }
        }
        FileUtil.writeFileFinishView(this, TAG)
    }

    private fun initView() {
        titleTextView = findViewById(R.id.eventDetailTitle)
        locationTextView = findViewById(R.id.locationTextView)
        dateTimeTextView = findViewById(R.id.dateTimeTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        favriteButton = findViewById(R.id.favoriteImageButton)
        favriteButton.setOnClickListener {
            onClickFavorite()
        }
    }

    private fun setDB() {
        eventId = intent.getIntExtra("eventId", -1)
        CoroutineScope(Dispatchers.IO).launch {
            val event = db.eventDao().getEventById(eventId)
            Log.d(TAG, "event:$event")
            event?.let {
                withContext(Dispatchers.Main) {
                    titleTextView.text = it.name
                    locationTextView.text = it.location
                    locationStr = it.location
                    val timeInt = it.dateTime
                    dateTimeTextView.text = TimeUtil.getDateString(timeInt)
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
