package com.kk.eventurmr

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.FileUtil
import com.kk.data.TimeUtil
import com.kk.data.UserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class SignInActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private var email: String = ""
    private var password: String = ""
    private val TAG = "SignInActivity"
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        )
            .build()
    }
    private val eventDBUtil by lazy { com.kk.data.EventDBUtil(db) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileUtil.writeFileStartView(this, TAG)
        setContentView(R.layout.activity_signin) // Set the content view to the sign-in layou
        setupViews()
        signInButton.setOnClickListener {
            checkIfUserIsSignedIn()
//            val intent = intent
//            intent.setClass(this@SignInActivity, MainActivity::class.java)
//            startActivity(intent)
        }
        signUpButton.setOnClickListener {
            val intent = intent
            intent.setClass(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        viewUserDB()
        getInfoFormURL()
        FileUtil.writeFileFinishView(this, TAG)
    }

    private fun getInfoFormURL() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Database operation: clear all events
                db.eventDao().clearAllEvents()
                // Network operation: fetch the document
                val document: Document =
                    Jsoup.connect("https://events.ucmerced.edu/calendar/1.xml").get()
                val nodes = document.select("channel")
                val items = nodes.select("item")
                val itemLength = items.size

                for (i in 0 until itemLength) {
                    var title = items[i].select("title").text()
                    if (title.contains("2023: ")) {
                        val temp = title.split("2023: ")
                        title = temp[1]
                    } else if (title.contains("2024:")) {
                        continue
                    }

                    if (title.contains("at")) {
                        title = title.substringBefore("at")
                    }
                    val cdataContent = items[i].select("description").text()
                    val parsedCdata = Jsoup.parse(cdataContent)
                    val firstParagraph = parsedCdata.select("p").first().text()
                    val time = items[i].select("pubDate").text()
                    val timeInt = TimeUtil.convertDateFormat(time)
                    val link = items[i].select("link").text()

                    // Generate the next ID for the event
                    val id = db.eventDao().getNextId() + 1
                    // Create an event object
                    val event = Event(id, title, link, timeInt, firstParagraph, false)
                    Log.d(TAG, "Event: $event")
                    // Insert the event into the database
                    db.eventDao().insertEvent(event)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun viewUserDB() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val users = db.userDao().getAllUsers()
                Log.d(TAG, "Users: $users")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupViews() {
        emailEditText = findViewById(R.id.signInemailEditText)
        passwordEditText = findViewById(R.id.signInpasswordEditText)
        signInButton = findViewById(R.id.signInButton)
        signUpButton = findViewById(R.id.signInToSignUpButton)
        signUpButton.background.alpha = 0
    }

    private fun checkIfUserIsSignedIn() {
        email = emailEditText.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
        } else if (passwordEditText.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
        } else {
            checkPassword()
        }
    }

    private fun checkPassword() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var dbPassword = db.userDao().getPasswordByEmail(email)
                password = passwordEditText.text.toString()
                when (dbPassword) {
                    null -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignInActivity, "Wrong email", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    password -> {
                        val id = db.userDao().getIdByEmail(email)
                        UserId.id = id!!
                        Log.d(TAG, "UserId: ${UserId.id}")
//                        eventDBUtil.addEventsToDatabase(lifecycleScope)
                        val fIds = db.favoriteDao().getEidByUid(UserId.id)
                        Log.d(TAG, "fIds: $fIds")
                        db.eventDao().updateFavorites(fIds)
                        val events = db.eventDao().getFavoriteEvents()
                        for (event in events) {
                            Log.d(TAG, "FavoriteEvent: $event")
                        }
                        // go to MainActivity
                        val intent = intent
                        intent.setClass(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {
                        Log.d(TAG, "Password: $password")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@SignInActivity,
                                "Wrong password",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignInActivity, "Wrong password", Toast.LENGTH_SHORT).show()
                }
                Log.d("SignInActivity", "Exception: $e")
            }
        }
    }

}