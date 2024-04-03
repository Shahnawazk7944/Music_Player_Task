package com.example.music_player_task.songs.presentation.product_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.music_player_task.R
import com.example.music_player_task.navigation.Screen
import com.example.music_player_task.songs.domain.model.Song
import com.example.music_player_task.songs.presentation.util.components.LoadingDialog
import com.example.music_player_task.songs.presentation.util.components.MyTopAppBar
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerStates
import com.example.music_player_task.songs.presentation.viewModels.SongViewModel
import com.example.music_player_task.ui.ubuntu

@Composable
internal fun ForYouScreen(
    viewModel: SongViewModel,
    navController: NavHostController,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    ForYouScreenContent(state = state, navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouScreenContent(
    state: MusicPlayerStates, navController: NavHostController,
) {
    LoadingDialog(isLoading = state.isLoading)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                title = {
                    Text(
                        "For You",
                        fontFamily = ubuntu,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                appBarLeadingIcon = painterResource(R.drawable.menu),
                onClick = {
                    navController.navigateUp()
                },
                action = {

                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(5.dp),
        ) {

            items(state.songs!!.data.size) { index ->
                val songImageKey = state.songs.data[index].cover
                Box(Modifier.clickable {
                    navController.navigate(
                        Screen.SongScreen.passToSongScreen(songImageKey)
//                        StoreScreen.StoreProductDetails.passToProductDetailsScree(
//                            index + 1
//                        )
                    ) // Navigate with index
                }) {
                    SongCard(song = state.songs.data[index])
                }

            }
        }
    }
}

@Composable
fun SongCard(
    song: Song
) {

}