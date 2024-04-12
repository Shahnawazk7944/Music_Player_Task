package com.example.music_player_task.songs.presentation.music_player_screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.music_player_task.R
import com.example.music_player_task.songs.domain.model.Song
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerStates
import com.example.music_player_task.songs.util.Constant
import com.example.music_player_task.ui.poppins

@Composable
fun SongPlayPauseCard(
    song: Song,
    index: Int,
    state: MusicPlayerStates,
    openSongScreen: (openSongScreen: Boolean, index: Int) -> Unit,
    pauseSong: (pausedSong: Boolean) -> Unit,
    playAgain: (song: String) -> Unit,
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
                            model = Constant.BASE_URL + "assets/" + song.cover,
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
                            model = Constant.BASE_URL + "assets/" + song.cover,
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
                if (state.playingSongDuration.value == 0) {
                    IconButton(
                        modifier = Modifier.scale(1.5f),
                        onClick = {
                        playAgain(song.url)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.restart),
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                                .weight(4f),
                            tint = Color.Unspecified
                        )
                    }
                } else {
                    if (state.isSongPlaying.value) {
                        IconButton(
                            modifier = Modifier.scale(1.5f),
                            onClick = {
                            pauseSong(true)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.pause),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .weight(4f),
                                tint = Color.Unspecified
                            )
                        }
                    } else {
                        IconButton(
                            modifier = Modifier.scale(1.5f),
                            onClick = {
                            pauseSong(false)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.play),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .weight(4f),
                                tint = Color.Unspecified
                            )
                        }
                    }
                }

            }
        }

    }


}