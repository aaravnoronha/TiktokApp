package com.example.tiktokapp2

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(private val videos: List<Video>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

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
            tvLikeCount.text = video.likes.toString()
            tvCommentCount.text = video.comments.toString()

            // Set up video
            try {
                val uri = Uri.parse(video.videoUrl)
                videoView.setVideoURI(uri)
                videoView.setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = true
                    videoView.start()
                }
            } catch (e: Exception) {
                // Handle video error
            }

            // Like button click
            ivLike.setOnClickListener {
                video.likes++
                tvLikeCount.text = video.likes.toString()
            }
        }
    }
}