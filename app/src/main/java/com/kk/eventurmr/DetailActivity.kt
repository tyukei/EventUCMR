package com.kk.eventurmr

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DetailActivity : BaseActivity() {
    private lateinit var title: TextView
    private lateinit var heartIcon: ImageView
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Replace with your actual layout resource name
        title = findViewById(R.id.eventDetailTitle)
        title.text = intent.getStringExtra("eventName") ?: "None"
        heartIcon = findViewById(R.id.heartIcon)
        registerButton = findViewById(R.id.registerButton)

        heartIcon.setOnClickListener {
            // Handle heart (favorite) icon click
            // For example, toggle the 'favorite' state of the event
            Toast.makeText(this, "Heart clicked!", Toast.LENGTH_SHORT).show()
        }

        registerButton.setOnClickListener {
            // Handle register button click
            // For example, navigate to a registration form or activity
            Toast.makeText(this, "Register button clicked!", Toast.LENGTH_SHORT).show()
        }
        setupMenuBar()
    }
}
