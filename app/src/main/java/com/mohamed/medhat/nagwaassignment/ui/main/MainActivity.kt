package com.mohamed.medhat.nagwaassignment.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.nagwaassignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Represents the home screen of the application. It contains the list of the available learning resources.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}