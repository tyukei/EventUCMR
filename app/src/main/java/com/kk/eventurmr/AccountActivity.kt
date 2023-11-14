package com.kk.eventurmr

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kk.data.AppDatabase
import com.kk.data.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountActivity : BaseActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        )
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setupViews()
        setupMenuBar()
        highlightSelectedIcon(R.id.profileImageView)
    }

    private fun setupViews() {
        nameTextView = findViewById(R.id.nameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val id = UserId.id
                val user = db.userDao().getUserById(id)
                withContext(Dispatchers.Main) {
                    nameTextView.text = user?.name ?: "No name"
                    emailTextView.text = user?.email ?: "No email"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
