package com.example.music_player_task.songs.presentation.music_player_screens

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.music_player_task.R
import com.example.music_player_task.songs.domain.model.Song
import com.example.music_player_task.songs.presentation.util.components.LoadingDialog
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerStates
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerUiEvents
import com.example.music_player_task.songs.presentation.viewModels.SongViewModel
import com.example.music_player_task.songs.util.Constant.BASE_URL
import com.example.music_player_task.ui.poppins
import com.example.music_player_task.ui.ubuntu
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ir.mahozad.multiplatform.wavyslider.WaveDirection
import ir.mahozad.multiplatform.wavyslider.material3.WavySlider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
internal fun ForYouScreen(
    viewModel: SongViewModel,
    navController: NavHostController,
) {
    ForYouScreenContent(navController = navController, viewModel = viewModel)
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
        val scope2 = rememberCoroutineScope()
        var openSongSheet by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = state.playingSongCurrentPosition) {
            viewModel.state.collectLatest {
                Log.d("check for song playing position", "${it.playingSongCurrentPosition.value}")
            }
        }


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

                            SongCard(
                                song = state.songs!!.data[index],
                                index = index,

                                ) { index, song ->
                                // viewModel.onEvent(event = MusicPlayerUiEvents.GetColorsFromImage(imageUrl = state.songs!!.data[index].url,context))
                                viewModel.onEvent(event = MusicPlayerUiEvents.SelectTheSong(state.songs!!.data[index]))
                                viewModel.onEvent(event = MusicPlayerUiEvents.PlaySong(state.songs!!.data[index].url))
                                viewModel.onEvent(event = MusicPlayerUiEvents.PlayingSongIndex(index))


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
                            }) {
                            if (it) {
                                viewModel.onEvent(event = MusicPlayerUiEvents.IsSongPlaying(false))
                                viewModel.onEvent(event = MusicPlayerUiEvents.PauseSong(true))
                            } else {
                                viewModel.onEvent(event = MusicPlayerUiEvents.IsSongPlaying(true))
                                viewModel.onEvent(event = MusicPlayerUiEvents.PauseSong(false))
                            }
                        }
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

                PlayingSongSheet(songIndex = state.playingSongIndex.value, state) {
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayingSongSheet(songIndex: Int, state: MusicPlayerStates, closeSheet: () -> Unit) {
    val context = LocalContext.current
    var gradientColors by remember {
        mutableStateOf<List<Color>>(emptyList())
    }
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme()


    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color(0xFF436c89),
            darkIcons = !useDarkIcons
        )

        // setStatusBarsColor() and setNavigationBarsColor() also exist
    }
//    val painter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(context)
//            .data(state.songs!!.data[songIndex].url)
//            .allowHardware(false)
//            .size(coil.size.Size.ORIGINAL)
//            .build()
//    )
//    Log.d("check", "cross image call")
//    val baseImageloadState = painter.state
//    if (baseImageloadState is AsyncImagePainter.State.Success){
//        val baseImageBitmap =
//            baseImageloadState.result.drawable.toBitmap()
//        val colors = listOf(
//            Palette.from(baseImageBitmap).generate().darkVibrantSwatch?.let { Color(it.titleTextColor) }
//                ?: Color.Gray,
//            Palette.from(baseImageBitmap).generate().dominantSwatch?.let { Color(it.bodyTextColor) }
//                ?: Color.Gray,
//            Palette.from(baseImageBitmap).generate().lightMutedSwatch?.let { Color(it.titleTextColor) }
//                ?: Color.Gray,
//            Palette.from(baseImageBitmap).generate().darkMutedSwatch?.let { Color(it.titleTextColor) }
//                ?: Color.Gray
//        )
//        gradientColors = colors
//    }

    val sheetBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF436c89),
            Color(0xFF8b2c40)
        )
    )

    val pagerState = rememberPagerState(pageCount = {
        state.songs!!.data.size
    })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(sheetBackground)
            .fillMaxSize()
    ) {

        //Image Pager -------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp), contentAlignment = Alignment.Center
        ) {

            HorizontalPager(
                modifier = Modifier,
                contentPadding = PaddingValues(horizontal = 60.dp),
                state = pagerState,
                pageSize = PageSize.Fixed(300.dp),
                //pageSpacing = 10.dp,
                verticalAlignment = Alignment.Bottom,

                ) { page ->
                SubcomposeAsyncImage(
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .graphicsLayer {
                            val pageOffSet = (
                                    (pagerState.currentPage - page) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                            )
                            scaleY = lerp(
                                start = 0.75f,
                                stop = 1f,
                                fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                            )
                        },
                    model = BASE_URL + "assets/" + state.songs!!.data[page].cover,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                )
            }
        }


        // Song name and Author
        Column(
            modifier = Modifier.padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.songs!!.data[pagerState.currentPage].name,
                fontFamily = poppins,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = state.songs.data[pagerState.currentPage].artist,
                fontFamily = ubuntu,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray
            )
        }

        Box(
            modifier =
            Modifier
                .padding(vertical = 80.dp, horizontal = 20.dp)
                .fillMaxWidth()
                .height(20.dp)
        ){
            var fraction by remember { mutableFloatStateOf(1f) }
            WavySlider(
                value = fraction,
                onValueChange = { fraction = it },
                waveLength = 25.dp,     // Set this to 0.dp to get a regular Slider
                waveHeight = 10.dp,     // Set this to 0.dp to get a regular Slider
                waveVelocity = 15.dp to WaveDirection.HEAD, // Speed per second and its direction
                waveThickness = 4.dp,   // Defaults to the specified trackThickness
                trackThickness = 4.dp,  // Defaults to 4.dp, same as regular Slider
                incremental = false,    // Whether to gradually increase waveHeight
                // animationSpecs = ... // Customize various animations of the wave
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.scale(1.5f),
                onClick = {

                }) {
                Icon(
                    painter = painterResource(R.drawable.nex),
                    contentDescription = null,
                    modifier = Modifier
                        .height(30.dp),
                    tint = Color(0xA6C0C4C7),
                )
            }
            if (false) {
                IconButton(
                    modifier = Modifier.scale(1.4f),
                    onClick = {

                    }) {
                    Icon(
                        painter = painterResource(R.drawable.play),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp),
                        tint = Color.Unspecified
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier.scale(1.4f),
                    onClick = {

                    }) {
                    Icon(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp),
                        tint = Color.Unspecified
                    )
                }
            }



            IconButton(
                modifier = Modifier.scale(1.5f),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.nex),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(180f),
                    tint = Color(0xA6C0C4C7),

                    )
            }
        }

    }
}

@Composable
fun SongPlayPauseCard(
    song: Song,
    index: Int,
    state: MusicPlayerStates,
    openSongScreen: (openSongScreen: Boolean, index: Int) -> Unit,
    pauseSong: (pausedSong: Boolean) -> Unit,
) {

    val songImageRotation = rememberInfiniteTransition(label = "")
    val angle by songImageRotation.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ), label = ""
    )
    val cardBackgroundColor = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF436c89),
            Color(0xFF8b2c40)
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 1.dp)
            .background(cardBackgroundColor)
            .clickable {
                openSongScreen(true, index)
            },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
//                .width(144.dp)
                    .height(48.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                ///Song Image ------------------------

                if (state.isSongPlaying.value) {
                    Box(modifier = Modifier.graphicsLayer {
                        rotationZ = angle
                    }) {
                        SubcomposeAsyncImage(
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            model = BASE_URL + "assets/" + song.cover,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(10.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                        )
                    }
                } else {
                    Box(modifier = Modifier) {
                        SubcomposeAsyncImage(
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            model = BASE_URL + "assets/" + song.cover,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(10.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                        )
                    }
                }


                /// Song Details -----------------
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = song.name,
                        fontFamily = poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

//                    Text(
//                        text = song.artist,
//                        fontFamily = ubuntu,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal,
//                        color = Color.Gray
//                    )
                }


                /// Song Play Pause --------------------
                if (state.isSongPlaying.value) {
                    IconButton(onClick = {
                        pauseSong(true)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.pause),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .weight(4f),
                            tint = Color.Unspecified
                        )
                    }
                } else {
                    IconButton(onClick = {
                        pauseSong(false)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.play),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .weight(4f),
                            tint = Color.Unspecified
                        )
                    }
                }

            }
        }

    }


}

@Composable
fun SongCard(
    song: Song,
    index: Int,
    onClick: (index: Int, song: Song) -> Unit

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 1.dp)
            .clickable {
                onClick(index, song)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
//                .width(144.dp)
                    .height(48.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SubcomposeAsyncImage(
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    model = BASE_URL + "assets/" + song.cover,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 5.dp),
                ) {
                    Text(
                        text = song.name,
                        fontFamily = poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = song.artist,
                        fontFamily = ubuntu,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
            }
        }

    }
}


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