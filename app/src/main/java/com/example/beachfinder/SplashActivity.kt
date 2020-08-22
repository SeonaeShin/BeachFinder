package com.example.beachfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        SystemClock.sleep(300)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}