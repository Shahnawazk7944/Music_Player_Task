package com.example.androidjetpackcomposepracticeprojects.store.presentation.viewModels

import com.example.music_player_task.songs.domain.model.Product


data class ProductScreenState(
    val isLoading : Boolean = false,
    val product: List<Product> = emptyList(),
    val error: String? = null
)

