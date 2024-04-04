package com.example.music_player_task.songs.presentation.product_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.music_player_task.R
import com.example.music_player_task.navigation.Screen
import com.example.music_player_task.songs.domain.model.Song
import com.example.music_player_task.songs.presentation.util.components.LoadingDialog
import com.example.music_player_task.songs.presentation.util.components.MyTopAppBar
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerStates
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerUiEvents
import com.example.music_player_task.songs.presentation.viewModels.SongViewModel
import com.example.music_player_task.ui.ubuntu

@Composable
internal fun ForYouScreen(
    viewModel: SongViewModel,
    navController: NavHostController,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    ForYouScreenContent(state = state, navController = navController, viewModel = viewModel)
}


@Composable
fun ForYouScreenContent(
    state: MusicPlayerStates, navController: NavHostController, viewModel: SongViewModel
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
        if (state.isLoading) {
            LoadingDialog(true)
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(5.dp),
            ) {

                items(state.songs!!.data.size) { index ->
                    val songImageKey = state.songs.data[index].cover
                    viewModel.onEvent(MusicPlayerUiEvents.GetSongImage(songImageKey))
                    Box(Modifier.clickable {
                        navController.navigate(
                            Screen.SongScreen.passToSongScreen(songImageKey)
//                        StoreScreen.StoreProductDetails.passToProductDetailsScree(
//                            index + 1
//                        )
                        ) // Navigate with index
                    }) {
                        if (state.isSongImageLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp).padding(10.dp),color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            SongCard(
                                song = state.songs.data[index],
                                //state.songImage
                            )
                        }

                    }

                }
            }
        }
    }
}

@Composable
fun SongCard(
    song: Song,
    //songImage: Bitmap?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.Black)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .width(144.dp)
                .height(48.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            songImage?.let {
//                Image(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .clip(RoundedCornerShape(12.dp)),
//                    contentDescription = "null",
//                    contentScale = ContentScale.Crop,
//                    bitmap = it.asImageBitmap()
//                )
//            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp),
            ) {
                Text(
                    text = song.name,
                    fontFamily = ubuntu,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = song.artist,
                    fontFamily = ubuntu,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSongCard() {

    SongCard(
        // songImage = null,
        song = Song(
            id = 1,
            status = "published",
            sort = null,
            user_created = "2085be13-8079-40a6-8a39-c3b9180f9a0a",
            date_created = "2023-08-10T06:10:57.746Z",
            user_updated = "2085be13-8079-40a6-8a39-c3b9180f9a0a",
            date_updated = "2023-08-10T07:19:48.547Z",
            name = "Colors",
            artist = "William King",
            accent = "#331E00",
            cover = "4f718272-6b0e-42ee-92d0-805b783cb471",
            top_track = true,
            url = "https://pub-172b4845a7e24a16956308706aaf24c2.r2.dev/august-145937.mp3"
        )
    )
}