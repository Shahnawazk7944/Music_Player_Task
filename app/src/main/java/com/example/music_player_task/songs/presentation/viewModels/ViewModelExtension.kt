package com.example.music_player_task.songs.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_player_task.songs.util.EventBus
import kotlinx.coroutines.launch

fun  ViewModel.sendEvent(event: Any){
    viewModelScope.launch{
        EventBus.sendEvent(event)
    }
}