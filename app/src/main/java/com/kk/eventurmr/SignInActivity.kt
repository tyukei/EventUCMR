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
import com.kk.data.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin) // Set the content view to the sign-in layou
        setupViews()
        signInButton.setOnClickListener {
            checkIfUserIsSignedIn()
        }
        signUpButton.setOnClickListener {
            val intent = intent
            intent.setClass(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        viewUserDB()
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
                        // go to MainActivity
                        val intent = intent
                        intent.setClass(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                        Log.d(TAG, "Password: $password")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignInActivity, "Wrong password", Toast.LENGTH_SHORT)
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