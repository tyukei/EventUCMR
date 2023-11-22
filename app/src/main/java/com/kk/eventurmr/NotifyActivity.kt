package com.kk.eventurmr

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.FileUtil
import com.kk.data.TimeUtil
import com.kk.eventurmr.list.EventAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotifyActivity : BaseActivity() {

    private lateinit var notifyListView: ListView
    private lateinit var adapter: EventAdapter
    private var TAG = "Upcomming"
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
        setContentView(R.layout.activity_notify) // make sure to use the correct layout file name
        initializeViews()
        setuptNotifyListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.notificationImageView)
        getNotifyEvent()
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
        notifyListView = findViewById(R.id.notifyListView)
    }

    private fun setuptNotifyListView() {
        val notifyItems = ArrayList<Event>()
        adapter = EventAdapter(this@NotifyActivity, notifyItems, db) // 初期化
        notifyListView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotifyEvent() {
        // get today like 2023-10-10-10:10
        val today = LocalDateTime.now()
        val fourWeek = LocalDateTime.now().plusWeeks(4)
        val formatToday = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val strToday = formatToday.format(today)
        val strFormat = format.format(fourWeek)
        val intToday = TimeUtil.getDateInt(strToday)
        val intFormat = TimeUtil.getDateInt(strFormat)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val within2weekEvent = db.eventDao().getFavoriteEventByTime(intToday, intFormat)
                withContext(Dispatchers.Main) {
                    updateListView(within2weekEvent)
                }
            } catch (e: Exception) {
                // Log the error or update the UI accordingly
                withContext(Dispatchers.Main) {
                    // Show error message to the user
                }
            }
        }
    }

    private fun updateListView(notifyEvents: List<Event>) {
        adapter.clear()
        adapter.addAll(notifyEvents) // リストを更新
        adapter.notifyDataSetChanged() // ListViewに変更を通知
        notifyListView.setOnItemClickListener { _, _, position, _ ->
            Log.d(TAG, "Item clicked: ${notifyEvents[position].name}")
            FileUtil.writeFile(
                applicationContext,
                TAG,
                "TAP",
                "Tap:${notifyEvents[position].name}"
            )
            val intent = Intent(this@NotifyActivity, DetailActivity::class.java)
            intent.putExtra("eventId", notifyEvents[position].id)
            startActivity(intent)
        }
    }


}
