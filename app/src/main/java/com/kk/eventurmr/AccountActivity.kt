package com.kk.eventurmr

import android.os.Bundle

class AccountActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setupMenuBar()
        highlightSelectedIcon(R.id.profileImageView)
    }
}
