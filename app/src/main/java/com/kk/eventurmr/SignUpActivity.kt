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
import com.kk.data.User
import com.kk.data.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button

    private val TAG = "SignUpActivity"

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
    }

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

    private fun setupViews() {
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
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
                val id = db.userDao().getNextId()
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
}