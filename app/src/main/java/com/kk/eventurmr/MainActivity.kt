package com.kk.eventurmr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.TimeUtil
import com.kk.data.UserId.Companion.id
import com.kk.eventurmr.list.EventAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"
    private lateinit var eventsListView: ListView

    // Roomデータベースのインスタンスを作成
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        )
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Use the correct layout file name here
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
        setupListView()
        setupMenuBar()
        highlightSelectedIcon(R.id.homeImageView)
        //clearAllEvents()
        addEventToDatabase()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val tapInfo =
                "Time: " + System.currentTimeMillis() + ", X: " + event.x + ", Y: " + event.y
            writeFile(tapInfo)
        }
        return super.onTouchEvent(event)
    }

    private fun writeFile(data: String) {
        Log.d(TAG, "writeFile: $data")
        try {
            val fos = openFileOutput("tap_log.txt", MODE_APPEND)
            fos.write(
                """$data
    """.toByteArray()
            )
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setupListView() {
        eventsListView = findViewById(R.id.eventsListView)

        lifecycleScope.launch {
            try {
                val events = getEventsFromDatabase()

                // UIスレッドでアダプター設定
                withContext(Dispatchers.Main) {
                    val adapter = EventAdapter(this@MainActivity, events)
                    eventsListView.adapter = adapter
                    eventsListView.setOnItemClickListener { _, _, position, _ ->
                        Log.d(TAG, "Item clicked: ${events[position].name}")
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra("eventId", events[position].id)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching events from database", e)
            }
        }
    }


    // データベースからイベントを取得する非同期関数
    private suspend fun getEventsFromDatabase(): List<Event> {
        return withContext(Dispatchers.IO) {
            db.eventDao().getAllEvents()
        }
    }

    // Clear db
    private fun clearAllEvents() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                db.eventDao().clearAllEvents()
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing events from database", e)
            }
        }
    }


    private fun addEventToDatabase() {
        val eventsList = listOf(
            Event(
                id = 1,
                name = "Business Society Internship Process at COB2 130",
                location = "UCMerced",
                dateTime = TimeUtil.getDateInt("2023-11-20 09:31"),
                description = "This is an 8 week internship process that involves two meetings a week. During these meetings, guest speakers like professors or Business Society alumni are brought in. Workshops are also conducted to help interns grow their skills and learn more about the professional world.",
                isfavorite = false
            ),
            Event(
                id = 2,
                name = "ES M.S. Thesis Defense; Mariela Colombo at SE2",
                location = "SE2, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 10:00"),
                description = "Abstract: The increasing deployment of variable renewable energies, such as solar PV and wind, provides a pathway for mitigating emissions in the power sector, as well as new challenges for system operators as they try to balance fluctuating supply and demand. ... Mariela is currently a Fulbright scholar and has been selected as a Future Energy Leader by the World Energy Council.",
                isfavorite = false
            ),
            Event(
                id = 3, // Incrementing the ID for each new event
                name = "Bobcat Blessing at Academic Walk",
                location = "Academic Walk, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 10:00"), // Assuming the event starts at 10:00 AM
                description = "Hey Bobcats! What are you thankful for? Find OSI Tabling on academic walk from 10 am to 3 pm and write down your blessing on a bobcat paw!",
                isfavorite = false
            ),
            Event(
                id = 4, // Setting the ID for this event
                name = "Matcha Pocky Fundraiser at In Front of Kolligian Library",
                location = "In Front of Kolligian Library, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 11:00"), // Assuming the event starts at 11:00 AM
                description = "We will be selling Double Rich Matcha Pocky (green tea cream covered green tea biscuit sticks) as a fundraiser for Yamabuki Taiko. Please check us out on our Instagram @yamabukitaiko for future fundraisers.",
                isfavorite = false
            ),
            Event(
                id = 5, // Assigning the ID for this event
                name = "PAA Choir Practice at GRAN 135",
                location = "GRAN 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 13:11"), // Assuming the event starts at 1:11 PM
                description = "PAA's Choir Division is one third of the CMCDs that make up presentations at the annual barrio fiesta hosted every November by PAA. Members will be using this room and space to practice their performances for the upcoming Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 6, // Setting the ID for this event
                name = "PAA Modern Practice at SSM 116",
                location = "SSM 116, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 14:00"), // Assuming the event starts at 2:00 PM
                description = "PAA's Modern Division is one third of the CMCDs that make up presentations at the annual barrio fiesta hosted every November by PAA. Only thing required is dance space and speakers. Usually our Modern leaders bring their own speakers and equipment for practice, nothing needs to be specially requested.",
                isfavorite = false
            ),
            Event(
                id = 7,
                name = "Study Abroad 101: General Info Session at KL-362",
                location = "KL-362, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 15:30"),
                description = "Interested in studying abroad but not sure where to start? Come to this info session led by a study abroad advisor. Topics covered include what is study abroad, why you should study abroad, options available, how to select the right program, and how to search and apply for programs.",
                isfavorite = false
            ),
            Event(
                id = 8,
                name = "Hermana Talk at ADMIN 354",
                location = "ADMIN 354, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 17:30"),
                description = "Weekly meeting for bonding experience among GB members by having workshops and group activities.",
                isfavorite = false
            ),
            Event(
                id = 9,
                name = "PAA Choir Practice at GLCR 135",
                location = "GLCR 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 20:00"),
                description = "PAA's Choir Division is one third of the CMCDs that make up presentations at the annual barrio fiesta hosted every November by PAA. Members will be using this room and space to practice their performances for the upcoming Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 10,
                name = "PAA Cultural Practice at GLCR 165",
                location = "GLCR 165, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 20:00"),
                description = "PAA's Cultural Division is one third of the CMCDs that make up presentations at the annual barrio fiesta hosted every November by PAA. This event is a space for them to practice their choreographies and interact with their team in preparation for Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 11,
                name = "PAA Modern Practice at Tenaya 145",
                location = "Tenaya 145, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-20 20:00"),
                description = "PAA's Modern Division is one third of the CMCDs that make up presentations at the annual barrio fiesta hosted every November by PAA. Only thing required is dance space and speakers. Usually our Modern leaders bring their own speakers and equipment for practice.",
                isfavorite = false
            )
        )



        lifecycleScope.launch(Dispatchers.IO) {
            try {
                eventsList.forEach { event ->
                    db.eventDao().insertEvent(event)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding event", e)
            }
        }
    }

}