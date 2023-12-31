package com.kk.eventurmr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.MotionEventCompat
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
    private var lastaction = 0

    private var eventId: Int = -1
    private var eventName:String =""
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
            FileUtil.writeFileButton(applicationContext, TAG, "MAP")
            FileUtil.writeFileFinishActivity(applicationContext, TAG,"WebActivity")
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(locationStr)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // この行を追加
                applicationContext.startActivity(it)
            }
        }
        FileUtil.writeFileFinishView(this, TAG)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        //https://yusuke-hata.hatenablog.com/entry/2014/12/05/234032
        val action: Int = MotionEventCompat.getActionMasked(event)
        if (lastaction == action) {
            return super.dispatchTouchEvent(event)
        } else {
            lastaction = action
            return when (action) {
                MotionEvent.ACTION_DOWN -> {
                    FileUtil.writeFile(applicationContext, TAG, "ACTION_DOWN", "")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_DOWN")
                    super.dispatchTouchEvent(event)
                }

                MotionEvent.ACTION_UP -> {
                    FileUtil.writeFile(applicationContext, TAG, "ACTION_UP", "")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_UP")
                    super.dispatchTouchEvent(event)
                }

                MotionEvent.ACTION_MOVE -> {
                    FileUtil.writeFile(applicationContext, TAG, "ACTION_MOVE", "")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_MOVE")
                    super.dispatchTouchEvent(event)
                }

                MotionEvent.ACTION_CANCEL -> {
                    FileUtil.writeFile(applicationContext, TAG, "ACTION_CANCEL", "")
                    Log.d(TAG, "dispatchTouchEvent: ACTION_CANCEL")
                    super.dispatchTouchEvent(event)
                }

                else -> super.dispatchTouchEvent(event)
            }
        }
    }

    private fun initView() {
        titleTextView = findViewById(R.id.eventDetailTitle)
        locationTextView = findViewById(R.id.locationTextView)
        dateTimeTextView = findViewById(R.id.dateTimeTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        favriteButton = findViewById(R.id.favoriteImageButton)
        favriteButton.setOnClickListener {
            FileUtil.writeFileButton(applicationContext, TAG, "FAVORITE")
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
                    eventName = it.name
                    titleTextView.text = eventName
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
                    FileUtil.writeFileUnFavorite(applicationContext,TAG, eventName)
                    db.eventDao().updateUnFavorite(eventId)
                    isfavorite = false
                    favriteButton.setBackgroundResource(R.drawable.ic_favorite)
                    var event = db.eventDao().getEventById(eventId)
                    Log.d(TAG, "event:$event")
                } else {
                    FileUtil.writeFileFavorite(applicationContext,TAG,eventName)
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
