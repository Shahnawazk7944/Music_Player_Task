package com.example.music_player_task.songs.presentation.viewModels

import com.example.music_player_task.songs.domain.model.Songs


data class MusicPlayerStates(
    val isLoading: Boolean = false,
    val songs: Songs? = null,
    val error: String? = null,
    var route:String = "forYou",
)

