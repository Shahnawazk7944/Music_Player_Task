package com.example.music_player_task.songs.presentation.music_player_screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.music_player_task.songs.presentation.util.components.LoadingDialog
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerUiEvents
import com.example.music_player_task.songs.presentation.viewModels.SongViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@Composable
internal fun ForYouScreen(
    viewModel: SongViewModel,
    navController: NavHostController,
) {
    ForYouScreenContent(navController = navController, viewModel = viewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouScreenContent(
    navController: NavHostController, viewModel: SongViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
//    LoadingDialog(isLoading = state.isLoading)
    val context = LocalContext.current
    Scaffold(
        containerColor = Color.Black,
        modifier = Modifier.fillMaxSize()
        //.windowInsetsPadding(WindowInsets.statusBarsIgnoringVisibility),
    ) { padding ->
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = isSystemInDarkTheme()

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Black,
                //darkIcons = !useDarkIcons
            )
        }
        val playingSongSheetState = rememberModalBottomSheetState(

            skipPartiallyExpanded = true
            // skipPartiallyExpanded = true for opening bottom sheet
            // state at fixed sized
        )
        val scope = rememberCoroutineScope()
        var openSongSheet by remember { mutableStateOf(false) }

        if (state.isLoading) {
            LoadingDialog(true)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .padding(top = 10.dp),
                        contentPadding = PaddingValues(5.dp),
                    ) {

                        items(state.songs!!.data.size) { index ->

                            // To Get Image Bitmap
//                            LaunchedEffect(key1 = index) {
//                                viewModel.onEvent(
//                                    event = MusicPlayerUiEvents.GetColorsFromImage(
//                                        state.songs!!.data[index].cover
//                                    )
//                                )
//
//                            }
                            SongCard(
                                song = state.songs!!.data[index],
                                index = index,

                                ) { index, song ->
                                viewModel.onEvent(event = MusicPlayerUiEvents.SelectTheSong(state.songs!!.data[index]))
                                viewModel.onEvent(event = MusicPlayerUiEvents.PlaySong(state.songs!!.data[index].url))
                                viewModel.onEvent(event = MusicPlayerUiEvents.changeSongIndex(index))


                            }
                        }
                    }
                }

                state.selectTheSong.value?.let { song ->
                    Box(
                        modifier = Modifier,
                        //contentAlignment = Alignment.BottomCenter
                    ) {
                        SongPlayPauseCard(
                            song = song,
                            state = state,
                            index = state.playingSongIndex.value,
                            openSongScreen = { openSongScreen, index ->
                                openSongSheet = openSongScreen
                            },
                            pauseSong = {
                                if (it) {
                                    viewModel.onEvent(
                                        event = MusicPlayerUiEvents.IsSongPlaying(
                                            false
                                        )
                                    )
                                    viewModel.onEvent(event = MusicPlayerUiEvents.PauseSong(true))
                                } else {
                                    viewModel.onEvent(event = MusicPlayerUiEvents.IsSongPlaying(true))
                                    viewModel.onEvent(event = MusicPlayerUiEvents.PauseSong(false))
                                }
                            },
                            playAgain = {
                                viewModel.onEvent(event = MusicPlayerUiEvents.PlaySong(it))
                            }
                        )
                    }
                }

            }
        }

        if (openSongSheet) {

            ModalBottomSheet(
                onDismissRequest = {
                    openSongSheet = false
                },
                Modifier
                    .fillMaxHeight()
                    .windowInsetsPadding(WindowInsets.statusBars),

                containerColor = Color.White,
                sheetState = playingSongSheetState,
                shape = BottomSheetDefaults.HiddenShape,
                scrimColor = Color.Black,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF436c89)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DragHandle,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp),
                            tint = Color.White
                        )
                    }

                },
//               windowInsets =
            ) {

                PlayingSongSheet(songIndex = state.playingSongIndex.value, viewModel, state) {
                    scope.launch { playingSongSheetState.hide() }
                        .invokeOnCompletion {
                            if (!playingSongSheetState.isVisible) {
                                openSongSheet = false
                            }
                        }

                }

            }
        }
    }
}

//suspend fun getDominantColors(imageUrl: String): List<Color> {
//    val result = await(coilImage(imageUrl)) // Use await for suspending function
//    val bitmap = result.value ?: return emptyList() // Handle null result
//    val palette = Palette.from(bitmap).dominantSwatch ?: return emptyList()
//    return listOf(palette.rgb, palette.bodyTextColor) // Extract dominant colors
//}


//@Preview(showBackground = true)
//@Composable
//fun PreviewSongCard() {
//
//    SongCard(
//        state = MusicPlayerStates()
////        songImage = null,
////        song = Song(
////            id = 1,
////            status = "published",
////            sort = null,
////            user_created = "2085be13-8079-40a6-8a39-c3b9180f9a0a",
////            date_created = "2023-08-10T06:10:57.746Z",
////            user_updated = "2085be13-8079-40a6-8a39-c3b9180f9a0a",
////            date_updated = "2023-08-10T07:19:48.547Z",
////            name = "Colors",
////            artist = "William King",
////            accent = "#331E00",
////            cover = "4f718272-6b0e-42ee-92d0-805b783cb471",
////            top_track = true,
////            url = "https://pub-172b4845a7e24a16956308706aaf24c2.r2.dev/august-145937.mp3"
////        )
//    )
//}