package com.example.tiktokapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var videoAdapter: VideoAdapter
    private val videoList = mutableListOf<Video>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        setupVideoList()
        setupViewPager()
    }

    private fun setupVideoList() {
        videoList.apply {
            add(Video(
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                title = "Amazing nature video 🌟",
                username = "naturelover",
                likes = 1250,
                comments = 89
            ))
            add(Video(
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                title = "Cool animation 🎬",
                username = "animator",
                likes = 890,
                comments = 42
            ))
            add(Video(
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                title = "Epic adventure 🔥",
                username = "adventurer",
                likes = 2100,
                comments = 156
            ))
        }
    }

    private fun setupViewPager() {
        videoAdapter = VideoAdapter(videoList)
        viewPager.adapter = videoAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }
}