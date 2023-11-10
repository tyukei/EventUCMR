package com.kk.eventurmr

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Note that we're not setting the content view here
    }

    protected fun setupMenuBar() {
        findViewById<ImageView>(R.id.homeImageView)?.setOnClickListener {
            navigateTo(MainActivity::class.java)
        }

        findViewById<ImageView>(R.id.searchImageView)?.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }

        findViewById<ImageView>(R.id.favoriteImageView)?.setOnClickListener {
            navigateTo(FavoriteActivity::class.java)
        }

        findViewById<ImageView>(R.id.notificationImageView)?.setOnClickListener {
            navigateTo(NotifyActivity::class.java)
        }

        findViewById<ImageView>(R.id.profileImageView)?.setOnClickListener {
            navigateTo(AccountActivity::class.java)
        }
    }

    private fun navigateTo(destination: Class<*>) {
        if (this::class.java != destination) {
            val intent = Intent(this, destination)
            startActivity(intent)
            overridePendingTransition(0, 0) // Optional: remove animations
        }
    }
}
