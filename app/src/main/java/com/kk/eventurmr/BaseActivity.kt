package com.kk.eventurmr

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kk.data.FileUtil

open class BaseActivity : AppCompatActivity() {
    private val TAG = "MenuBar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Note that we're not setting the content view here
    }
    protected fun highlightSelectedIcon(selectedImageViewId: Int) {
        // IDs of all image views in the menu bar
        val menuIcons = listOf(
            R.id.homeImageView,
            R.id.searchImageView,
            R.id.favoriteImageView,
            R.id.notificationImageView,
            R.id.profileImageView
        )

        // Drawable resources for default and selected states
        val defaultIconResources = listOf(
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_favorite,
            R.drawable.ic_notification,
            R.drawable.ic_profile
        )

        val selectedIconResources = listOf(
            R.drawable.ic_home_selected,
            R.drawable.ic_search_selected,
            R.drawable.ic_favorite_selected,
            R.drawable.ic_notification_selected,
            R.drawable.ic_profile_selected
        )

        // Reset all icons to default
        for (i in menuIcons.indices) {
            findViewById<ImageView>(menuIcons[i])?.setImageResource(defaultIconResources[i])
        }

        // Set selected icon
        findViewById<ImageView>(selectedImageViewId)?.setImageResource(
            selectedIconResources[menuIcons.indexOf(selectedImageViewId)]
        )
    }


    protected fun setupMenuBar() {
        val homeImageView = findViewById<ImageView>(R.id.homeImageView)
        homeImageView?.setOnClickListener {
            highlightSelectedIcon(R.id.homeImageView)
            navigateTo(MainActivity::class.java)
        }

        val searchImageView = findViewById<ImageView>(R.id.searchImageView)
        searchImageView?.setOnClickListener {
            highlightSelectedIcon(R.id.searchImageView)
            navigateTo(SearchActivity::class.java)
        }
        val favoriteImageView = findViewById<ImageView>(R.id.favoriteImageView)
        favoriteImageView?.setOnClickListener {
            highlightSelectedIcon(R.id.favoriteImageView)
            navigateTo(FavoriteActivity::class.java)
        }
        val notificationImageView = findViewById<ImageView>(R.id.notificationImageView)
        notificationImageView?.setOnClickListener {
            highlightSelectedIcon(R.id.notificationImageView)
            navigateTo(NotifyActivity::class.java)
        }
        val profileImageView = findViewById<ImageView>(R.id.profileImageView)
        profileImageView?.setOnClickListener {
            highlightSelectedIcon(R.id.profileImageView)
            navigateTo(AccountActivity::class.java)
        }
    }

    private fun navigateTo(destination: Class<*>) {
        if (this::class.java != destination) {
            val destinationName = destination.simpleName
            FileUtil.writeFileFinishActivity(applicationContext,TAG,destinationName)
            val intent = Intent(this, destination)
            startActivity(intent)
            overridePendingTransition(0, 0) // Optional: remove animations
        }
    }
}
