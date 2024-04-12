package com.example.music_player_task.songs.presentation.music_player_screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.SubcomposeAsyncImage
import com.example.music_player_task.R
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerStates
import com.example.music_player_task.songs.util.Constant
import com.example.music_player_task.ui.poppins
import com.example.music_player_task.ui.ubuntu
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ir.mahozad.multiplatform.wavyslider.WaveDirection
import ir.mahozad.multiplatform.wavyslider.material3.WavySlider
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
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
                    model = Constant.BASE_URL + "assets/" + state.songs!!.data[page].cover,
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

        AnimatedContent(targetState = pagerState.currentPage, transitionSpec = {
            (scaleIn() + fadeIn()) with (scaleOut() + fadeOut())
        }, label = "") {

            // Song name and Author
            Column(
                modifier = Modifier.padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = state.songs!!.data[it].name,
                    fontFamily = poppins,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = state.songs.data[it].artist,
                    fontFamily = ubuntu,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.LightGray
                )
            }
        }
        Box(
            modifier =
            Modifier
                .padding(vertical = 80.dp, horizontal = 20.dp)
                .fillMaxWidth()
                .height(20.dp)
        ) {
            var fraction by remember { mutableFloatStateOf(1f) }
            WavySlider(
                valueRange = 1000f..state.playingSongDuration.value.toFloat(),
                value = 1000f,
                onValueChange = { },
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
