package com.example.music_player_task.songs.presentation.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.music_player_task.songs.domain.model.Songs


data class MusicPlayerStates(
    val isLoading: Boolean = false,
    val isSongImageLoading: Boolean = false,
    val songs: Songs? = null,
    val error: String? = null,
    var route:String = "forYou",
    var songImage: Bitmap? = null,
    var songImages: MutableList<Bitmap?> = mutableListOf(),
)

