package com.example.music_player_task.songs.presentation.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.music_player_task.songs.domain.model.Song
import com.example.music_player_task.songs.domain.model.Songs
import java.time.Duration


data class MusicPlayerStates(
    val isLoading: Boolean = false,
    var isSongImageLoading: Boolean = false,
    val songs: Songs? = null,
    val error: String? = null,
    var route:String = "forYou",
    var songImage: Bitmap? = null,
    var songImages: MutableList<Bitmap?> = mutableListOf(),
    var selectTheSong: MutableState<Song?> = mutableStateOf(null),
    var isSongPlaying: MutableState<Boolean> = mutableStateOf(true),
    var playingSongIndex: MutableState<Int> = mutableIntStateOf(0),
    var gradientColors: List<androidx.compose.ui.graphics.Color> = emptyList(),
    var playingSongCurrentPosition: MutableState<Int> = mutableIntStateOf(0),
    var playingSongDuration: MutableState<Int> = mutableIntStateOf(0),
    var changeSongIndex: MutableState<Int> = mutableIntStateOf(0),
)

