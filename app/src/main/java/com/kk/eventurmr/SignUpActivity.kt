package com.kk.eventurmr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.EventDBUtil
import com.kk.data.FileUtil
import com.kk.data.TimeUtil
import com.kk.data.URLUtil
import com.kk.data.User
import com.kk.data.UserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class SignUpActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private var lastaction = 0

    private val TAG = "SignUpActivity"

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).fallbackToDestructiveMigration()
            .build()

    }
    private val eventDBUtil by lazy { EventDBUtil(db) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) // Set the content view to the sign-in layou
        setupViews()
        signInButton.setOnClickListener {
            finish()
        }
        signUpButton.setOnClickListener {
            checkIfUserIsSignedUp()
        }
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

    private fun setupViews() {
        nameEditText = findViewById(R.id.nameEditText)
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                FileUtil.writeFileKeyBoard(applicationContext,TAG,"NAME",s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        emailEditText = findViewById(R.id.emailEditText)
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                FileUtil.writeFileKeyBoard(applicationContext,TAG,"EMAIL",s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        passwordEditText = findViewById(R.id.passwordEditText)
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                FileUtil.writeFileKeyBoard(applicationContext,TAG,"PASSWORD",s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        signInButton = findViewById(R.id.signUpToSignInButton)
        signUpButton = findViewById(R.id.signUpButton)
        signInButton.background.alpha = 0
    }

    private fun checkIfUserIsSignedUp() {
        if (nameEditText.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
        } else if (emailEditText.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
        } else if (passwordEditText.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
        } else {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val id = db.userDao().getNextId() + 1
                val user = User(id, name, email, password)
                Log.d(TAG, "User: $user")
                db.userDao().insertUser(user)
                // check if user is registered
                var dbPassword = db.userDao().getPasswordByEmail(email)
                if (dbPassword == password) {
                    Log.d(TAG, "Password: $password")
                    val id = db.userDao().getIdByEmail(email)
                    UserId.id = id!!
                    // go to MainActivity
                    getInfoFormURL()
                    val intent = intent
                    intent.setClass(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUpActivity, "Wrong password", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d(TAG, "Exception: $e")
            }
        }
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

//                    if (title.contains("at")) {
//                        title = title.substringBefore("at")
//                    }
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

}