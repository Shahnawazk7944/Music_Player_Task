package com.example.music_player_task.songs.presentation.viewModels

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_player_task.songs.domain.repository.SongImageRepository
import com.example.music_player_task.songs.domain.repository.SongRepository
import com.example.music_player_task.songs.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            // Log.d("check songs", state.value.songs!!.data[1].name)
            _musicPlayerState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private var mediaPlayer: MediaPlayer? = null
    private var scope = viewModelScope
    //private var scope = MainScope()

    fun onEvent(event: MusicPlayerUiEvents) {
        when (event) {

            is MusicPlayerUiEvents.PlaySong -> {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        scope.coroutineContext.cancelChildren()
                        mediaPlayer?.stop()
                        mediaPlayer?.reset()
                        _musicPlayerState.update { state ->
                            state.copy(
                                playingSongCurrentPosition = state.playingSongCurrentPosition.apply {
                                    this.value = 0
                                },
                                playingSongDuration = state.playingSongDuration.apply {
                                    this.value = 0
                                }
                            )
                        }
                    }
                }
                _musicPlayerState.update { state ->
                    state.copy(
                        isSongPlaying = state.isSongPlaying.apply {
                            this.value = true
                        },
                        playingSongCurrentPosition = state.playingSongCurrentPosition.apply {
                            this.value = 0
                        },
                        playingSongDuration = state.playingSongDuration.apply {
                            this.value = 0
                        }
                    )
                }
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(event.url)
                    prepareAsync()
                }
                mediaPlayer?.setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.seekTo(state.value.playingSongCurrentPosition.value.toInt())
                    mediaPlayer.start()
                    setSongDuration(mediaPlayer.duration)
                    scope.launch { seekbarUpdateObserver() }
                   // updatePlaybackState(mediaPlayer.currentPosition.toFloat())

                    Log.d(
                        "check for currentD_VM",
                        "${state.value.playingSongCurrentPosition.value}"
                    )
                }

                mediaPlayer?.setOnSeekCompleteListener {
                    _musicPlayerState.update { state ->
                        state.copy(
                            playingSongCurrentPosition = state.playingSongCurrentPosition.apply {
                                this.value = it.currentPosition
                            }
                        )
                    }
                }

                mediaPlayer?.setOnCompletionListener { mediaPlayer ->
                    scope.coroutineContext.cancelChildren()
                    mediaPlayer?.stop()
                    _musicPlayerState.update { state ->
                        state.copy(
                            playingSongCurrentPosition = state.playingSongCurrentPosition.apply {
                                this.value = 0
                            },
                            playingSongDuration = state.playingSongDuration.apply {
                                this.value = 0
                            },
                            isSongPlaying = state.isSongPlaying.apply {
                                this.value = false
                            },
                        )
                    }
                }

            }


            is MusicPlayerUiEvents.PauseSong -> {

                mediaPlayer?.let {
                    _musicPlayerState.update { state ->
                        state.copy(
                            playingSongCurrentPosition = state.playingSongCurrentPosition.apply {
                                this.value = it.currentPosition
                            }
                        )
                    }

                    if (event.isPause) {
                        scope.coroutineContext.cancelChildren()
                        it.pause()
                    } else {
                        it.seekTo(state.value.playingSongCurrentPosition.value)
                        it.start()
                        scope.launch { seekbarUpdateObserver() }
                    }
                }
            }

            is MusicPlayerUiEvents.StopSong -> {
                scope.coroutineContext.cancelChildren()
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                _musicPlayerState.update { state ->
                    state.copy(
                        playingSongCurrentPosition = state.playingSongCurrentPosition.apply {
                            this.value = 0
                        },
                        playingSongDuration = state.playingSongDuration.apply {
                            this.value = 0
                        }
                    )
                }
            }

            is MusicPlayerUiEvents.ReleasePlayer -> {
                scope.coroutineContext.cancelChildren()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
                _musicPlayerState.update { state ->
                    state.copy(
                        playingSongCurrentPosition = state.playingSongCurrentPosition.apply {
                            this.value = 0
                        },
                        playingSongDuration = state.playingSongDuration.apply {
                            this.value = 0
                        }
                    )
                }
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

            is MusicPlayerUiEvents.IsSongPlaying -> {
                _musicPlayerState.update {
                    it.copy(
                        isSongPlaying = it.isSongPlaying.apply {
                            this.value = event.isSongPlaying
                        }
                    )
                }
            }

            is MusicPlayerUiEvents.PlayingSongIndex -> {
                _musicPlayerState.update {
                    it.copy(
                        playingSongIndex = it.playingSongIndex.apply {
                            this.value = event.playingSongIndex
                        }
                    )
                }
            }


            is MusicPlayerUiEvents.GetColorsFromImage -> {
                viewModelScope.launch {
                    _musicPlayerState.update { it.copy(isSongImageLoading = true) }

                    songImageRepository.getSongImage(event.url).onRight { bitmap ->
                        _musicPlayerState.update {
                            it.copy(
                                getImageBitmap = it.getImageBitmap.toMutableList().apply {
                                    add(bitmap!!)
                                }, isSongImageLoading = false
                            )

                        }
                        Log.d("check for colors", "${state.value.getImageBitmap.size}")

                    }.onLeft { error ->
                        _musicPlayerState.update {
                            it.copy(
                                error = error.error.message, isSongImageLoading = false
                            )
                        }
                        sendEvent(event = Event.Toast(error.error.message))
                    }
                }

            }

            is MusicPlayerUiEvents.UpdatePlaybackState -> {
                updatePlaybackState(event.currentPosition)
            }

            is MusicPlayerUiEvents.changeSongIndex -> {
                _musicPlayerState.update {
                    it.copy(
                        changeSongIndex = it.changeSongIndex.apply {
                            this.value = event.index
                        }
                    )
                }
            }
        }

    }
    private suspend fun seekbarUpdateObserver() {
        withContext(Dispatchers.IO) {
            while (true) {
                Log.d("check for dur", "${ state.value.playingSongCurrentPosition.value }")
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    val pos = mediaPlayer!!.currentPosition
                    //val progress = (pos.toFloat() / mediaPlayer!!.duration) * 100f
                    updatePlaybackState(pos)
                }

                //delay(17L)
                delay(1000)
            }
        }
    }
    private fun updatePlaybackState(currentPosition: Int) {
        _musicPlayerState.update {
            it.copy(
                playingSongCurrentPosition = it.playingSongCurrentPosition.apply {
                    this.value = currentPosition
                }
            )
        }
    }

    private fun setSongDuration(duration: Int) {
        _musicPlayerState.update {
            it.copy(
                playingSongDuration = it.playingSongDuration.apply {
                    this.value = duration
                }
            )
        }
    }

}