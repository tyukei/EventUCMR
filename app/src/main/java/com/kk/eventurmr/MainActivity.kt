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
        Log.d(TAG, "onTouchEvent: " + event.action)
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
            ),
            Event(
                id = 12,
                name = "Faculty & Graduate Student Write-In at COB2",
                location = "COB2-295, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 09:00"),
                description = "Faculty & Graduate Student Write-In held every Tuesday from 9 AM - 12 PM in COB2-295. Coffee & tea provided!",
                isfavorite = false
            ), Event(
                id = 13,
                name = "Study Abroad 101: General Info Session (Zoom)",
                location = "Online (Zoom)",
                dateTime = TimeUtil.getDateInt("2023-11-21 09:30"),
                description = "Interested in studying abroad but not sure where to start? Come to this info session led by a study abroad advisor. Topics covered include what is study abroad, why you should study abroad, options available, how to select the right program, and how to search and apply for programs.",
                isfavorite = false
            ), Event(
                id = 14,
                name = "Business Society Internship Process at COB2 130",
                location = "COB2 130, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 09:31:32"),
                description = "This is an 8-week internship process that involves two meetings a week. During these meetings, we bring in guest speakers like professors or Business Society alumni and conduct workshops to help interns grow their skills and learn more about the professional world.",
                isfavorite = false
            ), Event(
                id = 15,
                name = "Explore a Career in Education at Uncommon Schools!",
                location = "Online - UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 12:30:00"),
                description = "This external hosted employer event offers a chance to explore a career in education at Uncommon Schools. For more details and registration, please visit the provided link.",
                isfavorite = false
            ),
            Event(
                id = 16,
                name = "PAA Choir Practice at GRAN 135",
                location = "GRAN 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 13:11:36"),
                description = "PAA's Choir Division, a part of the CMCDs, practices for performances at the annual Barrio Fiesta. This event focuses on preparing for the upcoming fiesta.",
                isfavorite = false
            ),
            Event(
                id = 17,
                name = "PAA Modern Practice at SSM 116",
                location = "SSM 116, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 14:00:00"),
                description = "The Modern Division of PAA, part of the CMCDs, will hold a practice session for the annual Barrio Fiesta. Dance space and speakers are arranged by the modern leaders.",
                isfavorite = false
            ),
            Event(
                id = 18,
                name = "Hermana Talk at ADMIN 354",
                location = "ADMIN 354, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 17:30:00"),
                description = "A weekly meeting for GB members that includes workshops and group activities, aimed at fostering a bonding experience.",
                isfavorite = false
            ),
            Event(
                id = 19,
                name = "PAA Choir Practice at GLCR 135",
                location = "GLCR 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 20:00:00"),
                description = "Another session of PAA's Choir Division practice for the annual Barrio Fiesta, focusing on perfecting their performances.",
                isfavorite = false
            ),Event(
                id = 20,
                name = "PAA Cultural Practice at GLCR 165",
                location = "GLCR 165, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 20:00:00"),
                description = "PAA's Cultural Division practices for their presentations at the annual Barrio Fiesta. This event is a rehearsal space for choreographies and team interaction.",
                isfavorite = false
            ),
            Event(
                id = 21,
                name = "PAA Modern Practice at Tenaya 145",
                location = "Tenaya 145, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-21 20:00:00"),
                description = "The Modern Division of PAA practices for the annual Barrio Fiesta. The session requires dance space and speakers, usually brought by the modern leaders.",
                isfavorite = false
            ),
            Event(
                id = 22,
                name = "Business Society Internship Process at COB2 130",
                location = "COB2 130, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-22 09:31:32"),
                description = "An 8-week internship process including bi-weekly meetings with guest speakers like professors or Business Society alumni, and workshops for skill development.",
                isfavorite = false
            ),
            Event(
                id = 23,
                name = "PAA Choir Practice at GRAN 135",
                location = "GRAN 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-22 13:11:36"),
                description = "PAA's Choir Division practices for the Barrio Fiesta. This session focuses on refining the members' performances for the upcoming event.",
                isfavorite = false
            ),
            Event(
                id = 24,
                name = "PAA Modern Practice at SSM 116",
                location = "SSM 116, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-22 14:00:00"),
                description = "Practice session for PAA's Modern Division as part of their preparation for the annual Barrio Fiesta, requiring only dance space and speakers.",
                isfavorite = false
            ),
            Event(
                id = 25,
                name = "Hermana Talk at ADMIN 354",
                location = "ADMIN 354, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-22 17:30:00"),
                description = "Weekly bonding and development meeting for GB members, featuring workshops and group activities.",
                isfavorite = false
            ),
            Event(
                id = 26,
                name = "PAA Choir Practice at GLCR 135",
                location = "GLCR 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-22 20:00:00"),
                description = "Another choir practice session for PAA in preparation for the Barrio Fiesta, focusing on performance excellence.",
                isfavorite = false
            ),Event(
                id = 27,
                name = "PAA Cultural Practice at GLCR 165",
                location = "GLCR 165, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-22 20:00:00"),
                description = "PAA's Cultural Division practices for their annual Barrio Fiesta presentations, focusing on choreography and team interaction.",
                isfavorite = false
            ),
            Event(
                id = 28,
                name = "PAA Modern Practice at Tenaya 145",
                location = "Tenaya 145, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-22 20:00:00"),
                description = "The Modern Division of PAA prepares for the Barrio Fiesta, requiring only dance space and speakers for the practice.",
                isfavorite = false
            ),
            Event(
                id = 29,
                name = "Graduate Dissertation Writing Group at COB2",
                location = "COB2-295, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-23 09:00:00"),
                description = "A weekly dissertation writing group for graduates, providing a structured environment with coffee and tea.",
                isfavorite = false
            ),
            Event(
                id = 30,
                name = "PAA Choir Practice at GRAN 135",
                location = "GRAN 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-23 13:11:36"),
                description = "Choir Division of PAA practices for the upcoming Barrio Fiesta, focusing on refining performances.",
                isfavorite = false
            ),
            Event(
                id = 31,
                name = "PAA Modern Practice at SSM 116",
                location = "SSM 116, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-23 14:00:00"),
                description = "Another practice session for PAA's Modern Division in preparation for the Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 32,
                name = "Hermana Talk at ADMIN 354",
                location = "ADMIN 354, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-23 17:30:00"),
                description = "Weekly meeting for GB members at Hermana Talk, featuring workshops and group activities for bonding.",
                isfavorite = false
            ),
            Event(
                id = 33,
                name = "PAA Choir Practice at GLCR 135",
                location = "GLCR 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-23 20:00:00"),
                description = "PAA's Choir Division holds another practice session for the Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 34,
                name = "PAA Cultural Practice at GLCR 165",
                location = "GLCR 165, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-23 20:00:00"),
                description = "Cultural Division of PAA practices for Barrio Fiesta, focusing on choreography and team preparation.",
                isfavorite = false
            ),
            Event(
                id = 35,
                name = "PAA Modern Practice at Tenaya 145",
                location = "Tenaya 145, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-23 20:00:00"),
                description = "Practice session for PAA's Modern Division as they prepare for the Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 36,
                name = "MBA Dissertation Openhouse at Kolligan Library",
                location = "Kolligan Library, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 09:00:00"),
                description = "MBA dissertation workshop open to all UCM students. Seats available on a first-come, first-serve basis.",
                isfavorite = false
            ),
            Event(
                id = 37,
                name = "MBA Dissertation Openhouse",
                location = "UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 09:00:00"),
                description = "An openhouse for MBA dissertation workshops, effective every Friday throughout the semester.",
                isfavorite = false
            ),Event(
                id = 38,
                name = "PAA Choir Practice at GRAN 135",
                location = "GRAN 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 13:11:36"),
                description = "PAA's Choir Division, part of the CMCDs, holds a practice session for their performances at the upcoming Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 39,
                name = "PAA Modern Practice at SSM 116",
                location = "SSM 116, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 14:00:00"),
                description = "Practice session for PAA's Modern Division, focusing on dance and performance preparation for the Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 40,
                name = "Hermana Talk at ADMIN 354",
                location = "ADMIN 354, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 17:30:00"),
                description = "Weekly Hermana Talk meeting for GB members, featuring workshops and group activities for team bonding.",
                isfavorite = false
            ),
            Event(
                id = 41,
                name = "PAA Choir Practice at GLCR 135",
                location = "GLCR 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 20:00:00"),
                description = "Another choir practice session for PAA in preparation for the Barrio Fiesta, focusing on improving their performances.",
                isfavorite = false
            ),
            Event(
                id = 42,
                name = "PAA Cultural Practice at GLCR 165",
                location = "GLCR 165, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 20:00:00"),
                description = "PAA's Cultural Division's rehearsal session for the Barrio Fiesta, focusing on choreography and team interaction.",
                isfavorite = false
            ),
            Event(
                id = 43,
                name = "PAA Modern Practice at Tenaya 145",
                location = "Tenaya 145, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 20:00:00"),
                description = "PAA's Modern Division's practice session for the Barrio Fiesta, focusing on dance and performance skills.",
                isfavorite = false
            ),
            Event(
                id = 44,
                name = "PAA Modern Practice at ACS 108",
                location = "ACS 108, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-24 20:00:00"),
                description = "Another practice for PAA's Modern Division as part of the preparations for the Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 45,
                name = "PAA Choir Practice at GRAN 135",
                location = "GRAN 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-25 13:11:36"),
                description = "Continued practice session for PAA's Choir Division, working on their performances for the upcoming Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 46,
                name = "PAA Modern Practice at SSM 116",
                location = "SSM 116, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-25 14:00:00"),
                description = "PAA's Modern Division practices for the Barrio Fiesta, focusing on dance routines and performance techniques.",
                isfavorite = false
            ),
            Event(
                id = 47,
                name = "Hermana Talk at ADMIN 354",
                location = "ADMIN 354, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-25 17:30:00"),
                description = "Weekly gathering at Hermana Talk for GB members with workshops and group activities to enhance bonding and cooperation.",
                isfavorite = false
            ),
            Event(
                id = 48,
                name = "PAA Choir Practice at GLCR 135",
                location = "GLCR 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-25 20:00:00"),
                description = "Rehearsal for PAA's Choir Division as they prepare for their performance at the Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 49,
                name = "PAA Cultural Practice at GLCR 165",
                location = "GLCR 165, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-25 20:00:00"),
                description = "Rehearsal session for PAA's Cultural Division, focusing on their performance for the upcoming Barrio Fiesta.",
                isfavorite = false
            ),
            Event(
                id = 50,
                name = "PAA Modern Practice at Tenaya 145",
                location = "Tenaya 145, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-25 20:00:00"),
                description = "PAA's Modern Division continues their practice for the Barrio Fiesta, emphasizing dance and stage presence.",
                isfavorite = false
            ),
            Event(
                id = 51,
                name = "Chapter 11/26 at SSB 170",
                location = "SSB 170, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-26 17:00:31"),
                description = "Kappa Kappa Gamma chapter meeting to discuss announcements, events, and complete necessary tasks.",
                isfavorite = false
            ),
            Event(
                id = 52,
                name = "Hermana Talk at ADMIN 354",
                location = "ADMIN 354, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-26 17:30:00"),
                description = "Regular meeting for GB members at Hermana Talk, including various workshops and group activities.",
                isfavorite = false
            ),
            Event(
                id = 53,
                name = "PAA Choir Practice at GLCR 135",
                location = "GLCR 135, UC Merced",
                dateTime = TimeUtil.getDateInt("2023-11-26 20:00:00"),
                description = "Another practice session for PAA's Choir Division, preparing for their performance at the Barrio Fiesta.",
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