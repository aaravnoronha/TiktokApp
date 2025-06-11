package com.example.tiktokapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var videoAdapter: VideoAdapter
    private val videoService = VideoService()
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        setupViewPager()
        loadInitialVideos()
    }

    private fun setupViewPager() {
        videoAdapter = VideoAdapter(
            onLoadMore = {
                loadMoreVideos()
            }
        )

        viewPager.adapter = videoAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        // Add page change callback for better video management
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("TikTokApp", "Page selected: $position")
            }
        })
    }

    private fun loadInitialVideos() {
        if (isLoading) return

        Log.d("TikTokApp", "Loading initial videos...")
        isLoading = true

        lifecycleScope.launch {
            try {
                val response = videoService.getInitialVideos()
                videoAdapter.clearVideos()
                videoAdapter.addVideos(response.videos, response.hasMore)
                Log.d("TikTokApp", "Loaded ${response.videos.size} initial videos")
            } catch (e: Exception) {
                Log.e("TikTokApp", "Error loading initial videos", e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadMoreVideos() {
        if (isLoading) return

        Log.d("TikTokApp", "Loading more videos...")
        isLoading = true
        videoAdapter.setLoading(true)

        lifecycleScope.launch {
            try {
                val response = videoService.loadMoreVideos()
                videoAdapter.addVideos(response.videos, response.hasMore)
                Log.d("TikTokApp", "Loaded ${response.videos.size} more videos")
            } catch (e: Exception) {
                Log.e("TikTokApp", "Error loading more videos", e)
            } finally {
                isLoading = false
                videoAdapter.setLoading(false)
            }
        }
    }
}