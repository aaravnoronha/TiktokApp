package com.example.tiktokapp2

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(
    private val onLoadMore: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val videos = mutableListOf<Video>()
    private var isLoading = false
    private var hasMore = true
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val TYPE_VIDEO = 0
        private const val TYPE_LOADING = 1
    }

    fun addVideos(newVideos: List<Video>, hasMoreData: Boolean) {
        val startPosition = videos.size
        videos.addAll(newVideos)
        hasMore = hasMoreData
        notifyItemRangeInserted(startPosition, newVideos.size)
    }

    fun setLoading(loading: Boolean) {
        if (isLoading != loading) {
            isLoading = loading
            handler.post {
                if (loading) {
                    notifyItemInserted(videos.size)
                } else {
                    notifyItemRemoved(videos.size)
                }
            }
        }
    }

    fun clearVideos() {
        videos.clear()
        isLoading = false
        hasMore = true
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < videos.size) TYPE_VIDEO else TYPE_LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VIDEO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
                VideoViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoViewHolder -> {
                holder.bind(videos[position])

                // Load more when approaching end - use handler to avoid layout conflicts
                if (position >= videos.size - 2 && hasMore && !isLoading) {
                    handler.post {
                        if (!isLoading && hasMore) {
                            onLoadMore()
                        }
                    }
                }
            }
            is LoadingViewHolder -> {
                // Loading item, no action needed
            }
        }
    }

    override fun getItemCount(): Int {
        return videos.size + if (isLoading && hasMore) 1 else 0
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val videoView: VideoView = itemView.findViewById(R.id.videoView)
        private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        private val tvCommentCount: TextView = itemView.findViewById(R.id.tvCommentCount)
        private val ivLike: ImageView = itemView.findViewById(R.id.ivLike)

        fun bind(video: Video) {
            tvUsername.text = "@${video.username}"
            tvTitle.text = video.title
            tvLikeCount.text = formatCount(video.likes)
            tvCommentCount.text = formatCount(video.comments)

            // Set up video with error handling
            try {
                val uri = Uri.parse(video.videoUrl)
                videoView.setVideoURI(uri)
                videoView.setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = true
                    videoView.start()
                }
                videoView.setOnErrorListener { _, _, _ ->
                    // Handle video error gracefully
                    true
                }
            } catch (e: Exception) {
                // Handle video error
                e.printStackTrace()
            }

            // Like button click with animation
            ivLike.setOnClickListener {
                video.likes++
                tvLikeCount.text = formatCount(video.likes)

                // Simple scale animation
                ivLike.animate()
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(100)
                    .withEndAction {
                        ivLike.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start()
                    }
                    .start()
            }
        }

        private fun formatCount(count: Int): String {
            return when {
                count >= 1000000 -> "${(count / 1000000.0).toInt()}M"
                count >= 1000 -> "${(count / 1000.0).toInt()}K"
                else -> count.toString()
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}