package com.example.music_player_task.songs.presentation.viewModels

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
import okhttp3.internal.wait
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


    fun onEvent(event: MusicPlayerUiEvents) {
        when (event) {
            is MusicPlayerUiEvents.NavigateTo -> {
                _musicPlayerState.update {
                    it.copy(route = event.route)
                }
            }

            is MusicPlayerUiEvents.GetSongImage -> {
                _musicPlayerState.update { it.copy(isSongImageLoading = true) }

                viewModelScope.launch {
                    Log.d("check", "${state.value.isSongImageLoading}")
                    songImageRepository.getSongImage(event.imageId).onRight { image ->
                        _musicPlayerState.update {
                            it.copy(
                                songImages = it.songImages.toMutableList().apply {
                                    add(image!!)
                                    Log.d("check array size", "${state.value.songImages.size}")
                                },

                            )
                        }
                    }.onLeft { error ->
                        _musicPlayerState.update {
                            it.copy(
                                error = error.error.message,
                                isSongImageLoading = false
                            )
                        }
                        sendEvent(event = Event.Toast(error.error.message))
                    }
                }
                Log.d("check after", "${state.value.isSongImageLoading}")
                _musicPlayerState.update {
                    it.copy(
                        isSongImageLoading = false
                    )
                }


            }


        }


    }

}