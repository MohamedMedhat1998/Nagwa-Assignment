package com.mohamed.medhat.nagwaassignment.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.nagwaassignment.databinding.ActivityMainBinding
import com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityState
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
        initViews()
        registerObservers()
    }

    /**
     * Initializes the UI.
     */
    private fun initViews() {

    }

    /**
     * Subscribes to the observable fields in the [mainViewModel].
     */
    private fun registerObservers() {
        mainViewModel.state.observe(this) {
            when (it.state) {
                ActivityState.STATE_NORMAL -> {
                    binding.rvMainData.visibility = View.VISIBLE
                    binding.pbMainLoading.visibility = View.INVISIBLE
                    binding.tvMainError.visibility = View.INVISIBLE
                }
                ActivityState.STATE_LOADING -> {
                    binding.rvMainData.visibility = View.INVISIBLE
                    binding.pbMainLoading.visibility = View.VISIBLE
                    binding.tvMainError.visibility = View.INVISIBLE
                }
                ActivityState.STATE_ERROR -> {
                    binding.rvMainData.visibility = View.INVISIBLE
                    binding.pbMainLoading.visibility = View.INVISIBLE
                    binding.tvMainError.visibility = View.VISIBLE
                    binding.tvMainError.text = it.error
                }
            }
        }
    }
}