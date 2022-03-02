package com.artworkspace.github.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, MainActivity::class.java).also {
            runBlocking {
                delay(500)
                startActivity(it)
                finish()
            }
        }
    }
}