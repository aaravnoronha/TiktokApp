package com.example.tiktokapp2

data class Video(
    val id: String,
    val videoUrl: String,
    val title: String,
    val username: String,
    var likes: Int,
    val comments: Int
)