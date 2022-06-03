package com.example.newtrainerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newtrainerapp.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    lateinit var progressText: TextView
    var maxProgress = 100
    var progressStatus = 0
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        progressText = binding.progressText
        progressBar = binding.progressBar

        progress()

    }

    private fun progress() {
        val thread = Thread {
            while (!allCheck()) {
                count()
                runOnUiThread {
                    progressBar.max = maxProgress
                    progressBar.progress = progressStatus
                    progressText.text = "$progressStatus %"
                }
                if (allCheck()) {
                    finish()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        thread.start()
    }

    private fun allCheck(): Boolean {
        if (progressStatus == maxProgress) {
            return true
        }
        return false
    }

    private fun count() {
        progressStatus += 1
        Thread.sleep(25)
    }
}