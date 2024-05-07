package com.example.music_player_task.songs.presentation.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.music_player_task.songs.domain.model.Song
import com.example.music_player_task.songs.domain.model.Songs


data class MusicPlayerStates(
    val isLoading: Boolean = false,
    var isSongImageLoading: Boolean = false,
    val songs: Songs? = null,
    val error: String? = null,
    var route: String = "forYou",
    var songImage: Bitmap? = null,
    var getColorsFromImage: List<List<Color>> = emptyList(),
    var getImageBitmap: List<Bitmap> = emptyList(),
    //var getColorsFromImage: MutableList<List<Color>> = mutableListOf(),
    var selectTheSong: MutableState<Song?> = mutableStateOf(null),
    var isSongPlaying: MutableState<Boolean> = mutableStateOf(true),
    var playingSongIndex: MutableState<Int> = mutableIntStateOf(0),
    var gradientColors: List<Color> = emptyList(),
    var playingSongCurrentPosition: MutableState<Int> = mutableIntStateOf(0),
    var playingSongDuration: MutableState<Int> = mutableIntStateOf(0),
    var changeSongIndex: MutableState<Int> = mutableIntStateOf(0),
)

