package com.example.music_player_task.songs.presentation.music_player_screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.music_player_task.songs.presentation.util.components.LoadingDialog
import com.example.music_player_task.songs.presentation.viewModels.SongViewModel

@Composable
fun TopTrack(
    navController: NavHostController, viewModel: SongViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
//    LoadingDialog(isLoading = state.isLoading)
    Scaffold(
        containerColor = Color.Black,
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        if (state.isLoading) {
            LoadingDialog(true)
        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(top = 10.dp),
                contentPadding = PaddingValues(5.dp),
            ) {

                items(state.songs!!.data.size) { index ->

                    if(state.songs!!.data[index].top_track){
                        SongCard(
                            song = state.songs!!.data[index],

                            index = index,

                        ) { index, song ->


//                            Screen.SongScreen.passToSongScreen(
//                                songImageKey
//                            )

                        }
                    }

                }
            }
        }
    }
}