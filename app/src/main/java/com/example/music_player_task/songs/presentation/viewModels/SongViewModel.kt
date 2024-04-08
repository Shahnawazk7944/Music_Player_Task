package com.example.music_player_task.songs.presentation.viewModels

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_player_task.songs.domain.repository.SongImageRepository
import com.example.music_player_task.songs.domain.repository.SongRepository
import com.example.music_player_task.songs.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val songRepository: SongRepository, private val songImageRepository: SongImageRepository
) : ViewModel() {
    private val _musicPlayerState = MutableStateFlow(MusicPlayerStates())
    val state = _musicPlayerState.asStateFlow()

    init {
        getSongs()
    }

    private fun getSongs() {
        viewModelScope.launch {
            _musicPlayerState.update {
                it.copy(isLoading = true)
            }
            songRepository.getSongs().onRight { songs ->
                _musicPlayerState.update {
                    it.copy(
                        songs = songs
                    )
                }
            }.onLeft { error ->
                _musicPlayerState.update {
                    it.copy(
                        error = error.error.message
                    )
                }
                sendEvent(event = Event.Toast(error.error.message))
            }
            Log.d("check songs", state.value.songs!!.data[1].name)
            _musicPlayerState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private var mediaPlayer: MediaPlayer? = null
    val mediaMetadataRetriever = MediaMetadataRetriever()
    private var currentPosition = 0
    fun onEvent(event: MusicPlayerUiEvents) {
        when (event) {

            is MusicPlayerUiEvents.PlaySong -> {
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            mediaPlayer?.stop()
                            mediaPlayer?.reset()
                        }
                    }
                    mediaPlayer?.release()
                    mediaMetadataRetriever.setDataSource(event.url, null) // Set the URL as the data source
                    val durationString =
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    mediaMetadataRetriever.release()
                    val duration = durationString?.toLong() ?: 0
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(event.url)
                        prepareAsync()
                    }
                    mediaPlayer?.setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.seekTo(currentPosition)
                        mediaPlayer.start()
                    }

            }


            is MusicPlayerUiEvents.PauseSong -> {
                    mediaPlayer?.let {
                        currentPosition = it.currentPosition
                        it.pause()
                    }
            }

            is MusicPlayerUiEvents.StopSong -> {
                    mediaPlayer?.stop()
                    mediaPlayer?.reset()
                    currentPosition = 0
            }

            is MusicPlayerUiEvents.ReleasePlayer -> {
                    mediaPlayer?.reset()
                    mediaPlayer?.release()
                    mediaPlayer = null
                    currentPosition = 0
            }

            is MusicPlayerUiEvents.NavigateTo -> {
                _musicPlayerState.update {
                    it.copy(route = event.route)
                }
            }
            is MusicPlayerUiEvents.SelectTheSong -> {
                _musicPlayerState.update {
                    it.copy(
                        selectTheSong = it.selectTheSong.apply {
                            this.value = event.selectTheSong
                        }
                    )
                }
            }

//            is MusicPlayerUiEvents.GetSongImage -> {
//                viewModelScope.launch {
//                    _musicPlayerState.update { it.copy(isSongImageLoading = true) }
//
//                    songImageRepository.getSongImage(event.imageId).onRight { image ->
//                        _musicPlayerState.update {
//                            it.copy(
//                                songImages = it.songImages.toMutableList().apply {
//                                    add(image)
//                                }, isSongImageLoading = false
//                            )
//
//                        }
//                    }.onLeft { error ->
//                        _musicPlayerState.update {
//                            it.copy(
//                                error = error.error.message, isSongImageLoading = false
//                            )
//                        }
//                        sendEvent(event = Event.Toast(error.error.message))
//                    }
//                }
//
//            }
        }

    }

}