package com.mohamed.medhat.nagwaassignment.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.mohamed.medhat.nagwaassignment.BuildConfig
import com.mohamed.medhat.nagwaassignment.databinding.ActivityMainBinding
import com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


/**
 * Represents the home screen of the application. It contains the list of the available learning resources.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: ItemsAdapter

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
        binding.rvMainData.apply {
            layoutManager = LinearLayoutManager(baseContext)
            adapter = this@MainActivity.adapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        adapter.onDownloadClicked = {
            mainViewModel.downloadData(it)
        }
        adapter.onCancelDownloadClicked = {
            mainViewModel.cancelDownload(it)
        }
        adapter.onDeleteMediaClicked = {
            mainViewModel.deleteMedia(it)
        }
        adapter.onPlayVideoClicked = {
            val videoFile = File("$filesDir/${it.id}.${it.url.takeLast(3)}")
            val fileUri = FileProvider.getUriForFile(
                this,
                "${BuildConfig.APPLICATION_ID}.provider",
                videoFile
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(fileUri, "video/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
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

        mainViewModel.data.observe(this) {
            adapter.submitList(it)
        }

        mainViewModel.newItemState.observe(this) {
            adapter.updateItemState(it)
        }

        mainViewModel.toastMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}