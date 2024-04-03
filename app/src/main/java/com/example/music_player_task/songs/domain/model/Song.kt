package com.example.music_player_task.songs.domain.model

data class Songs(
    val data: List<Song>,
)

data class Song(
    val id: Int,
    val status: String,
    val sort : String?,
    val user_created: String,
    val date_created: String,
    val user_updated: String,
    val date_updated: String,
    val name: String,
    val artist: String,
    val accent: String,
    val cover: String,
    val top_track: Boolean,
    val url: String
)