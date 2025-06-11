package com.example.tiktokapp2

import kotlinx.coroutines.delay
import kotlin.random.Random

class VideoService {

    private val videoPool = listOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
    )

    private val usernames = listOf(
        "adventurer", "naturelover", "filmmaker", "explorer", "creator",
        "artist", "traveler", "foodie", "musician", "dancer", "gamer",
        "techie", "fitness", "comedian", "chef", "photographer"
    )

    private val titles = listOf(
        "Amazing sunset timelapse 🌅", "Epic adventure begins! 🔥", "Nature at its finest 🌿",
        "Incredible wildlife footage 🦁", "Behind the scenes magic ✨", "Stunning landscapes 🏔️",
        "Creative process revealed 🎨", "Journey to the unknown 🌍", "Mind-blowing effects 🤯",
        "Pure artistic expression 🎭", "Breathtaking moments 📸", "Life in motion 🎬",
        "Discover hidden gems 💎", "Passion project complete 🚀", "Unforgettable experience 💫"
    )

    private var currentPage = 0
    private val pageSize = 5

    suspend fun getVideos(page: Int = 0): VideoResponse {
        // Simulate network delay
        delay(1000)

        val videos = mutableListOf<Video>()
        val startIndex = page * pageSize

        repeat(pageSize) { index ->
            val videoIndex = (startIndex + index) % videoPool.size
            videos.add(
                Video(
                    id = "${page}_$index",
                    videoUrl = videoPool[videoIndex],
                    title = titles.random(),
                    username = usernames.random(),
                    likes = Random.nextInt(100, 5000),
                    comments = Random.nextInt(10, 200)
                )
            )
        }

        return VideoResponse(
            videos = videos,
            page = page,
            hasMore = page < 50 // Simulate 50 pages of content
        )
    }

    suspend fun loadMoreVideos(): VideoResponse {
        currentPage++
        return getVideos(currentPage)
    }

    suspend fun getInitialVideos(): VideoResponse {
        currentPage = 0
        return getVideos(0)
    }
}

data class VideoResponse(
    val videos: List<Video>,
    val page: Int,
    val hasMore: Boolean
)