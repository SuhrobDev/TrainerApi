package com.example.newtrainerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newtrainerapp.controller.Extensions
import com.example.newtrainerapp.databinding.ActivityMainBinding
import com.example.newtrainerapp.ui.TrainerFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Extensions.init(R.id.container, supportFragmentManager)
        Extensions.controller?.replaceFragment(TrainerFragment())
    }
}