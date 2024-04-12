package com.example.music_player_task.songs.presentation.viewModels

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

    fun onEvent(event: MusicPlayerUiEvents) {
        when (event) {

            is MusicPlayerUiEvents.PlaySong -> {
                mediaPlayer?.let {
                    if (it.isPlaying) {
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
                _musicPlayerState.update {state ->
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
                    mediaPlayer.seekTo(state.value.playingSongCurrentPosition.value)
                    mediaPlayer.start()
                    setSongDuration(mediaPlayer.duration)
                    updatePlaybackState(mediaPlayer.currentPosition)

                    Log.d("check for currentD_VM", "${state.value.playingSongCurrentPosition.value}")
                }

                mediaPlayer?.setOnCompletionListener { mediaPlayer ->
                    // Use for precise updates
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
                        it.pause()
                    } else {
                        it.seekTo(state.value.playingSongCurrentPosition.value)
                        it.start()
                    }
                }
            }

            is MusicPlayerUiEvents.StopSong -> {
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
//                viewModelScope.launch {
//                    val loader = ImageLoader(event.context)
//                    val req = ImageRequest.Builder(event.context)
//                        .data(event.imageUrl) // demo link
//                        .target { result ->
//                            val bitmap = (result as BitmapDrawable).bitmap
//
//                            val colors:List<Color> = listOf(
//                                Palette.from(bitmap).generate().darkVibrantSwatch?.let { Color(it.titleTextColor) }
//                                    ?: Color.Gray,
//                                Palette.from(bitmap).generate().dominantSwatch?.let { Color(it.bodyTextColor) }
//                                    ?: Color.Gray,
//                                Palette.from(bitmap).generate().lightMutedSwatch?.let { Color(it.titleTextColor) }
//                                    ?: Color.Gray,
//                                Palette.from(bitmap).generate().darkMutedSwatch?.let { Color(it.titleTextColor) }
//                                    ?: Color.Gray
//                            )
//                            Log.d("check", "check 2")
//                            Log.d("check", (bitmap==null).toString())
//
//                            _musicPlayerState.update {
//                                it.copy(
//                                    gradientColors = colors
//                                )
//                            }
//                        }
//                        .build()
//                    val disposable = loader.enqueue(req)
//
//                    Log.d("check", "check 1")
//
//
//
////                    val bitmap = (result as BitmapDrawable).bitmap
//
//                }
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