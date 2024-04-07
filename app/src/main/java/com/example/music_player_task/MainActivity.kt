package com.example.music_player_task

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.music_player_task.navigation.MusicPlayerNavGraph
import com.example.music_player_task.navigation.Screen
import com.example.music_player_task.songs.presentation.viewModels.MusicPlayerUiEvents
import com.example.music_player_task.songs.presentation.viewModels.SongViewModel
import com.example.music_player_task.songs.util.Event
import com.example.music_player_task.songs.util.EventBus
import com.example.music_player_task.ui.theme.Music_Player_TaskTheme
import com.example.music_player_task.ui.ubuntu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val songViewModel by viewModels<SongViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val lifecyclleOwner = LocalLifecycleOwner.current.lifecycle
            LaunchedEffect(key1 = lifecyclleOwner) {
                lifecyclleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    EventBus.event.collect { event ->
                        when (event) {
                            is Event.Toast -> {
                                Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }

            Music_Player_TaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // val songViewModel : SongViewModel = hiltViewModel()
                    //val songViewModel by viewModels<SongViewModel>()
                    val navController = rememberNavController()
                    val state by songViewModel.state.collectAsStateWithLifecycle()


                    Scaffold(
                        containerColor = Color.Black,
                        bottomBar = {
                            NavigationBar {

                                NavigationBarItem(
                                   colors = NavigationBarItemDefaults.colors(
                                       selectedIconColor = Color.White,
                                       unselectedIconColor = Color.Gray,
                                       selectedTextColor = Color.White,
                                       unselectedTextColor = Color.Gray,
                                       indicatorColor = MaterialTheme.colorScheme.inverseOnSurface
                                   ),
                                    enabled = true,
                                    selected = state.route == "forYou",
                                    onClick = {
                                        songViewModel.onEvent(MusicPlayerUiEvents.NavigateTo("forYou"))
                                        navController.navigate(Screen.ForYou.route)
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(R.drawable.dottt),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.Top)
                                                .padding(horizontal = 5.dp)
                                        )
                                    },
                                    label = {
                                            Text(
                                                text = "For You",
                                                fontFamily = ubuntu,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .padding(horizontal = 5.dp)
                                            )
                                    },

                                )

                                NavigationBarItem(
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        unselectedIconColor = Color.Gray,
                                        selectedTextColor = Color.White,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = MaterialTheme.colorScheme.inverseOnSurface
                                    ),
                                    selected = state.route == "topTrack",
                                    onClick = {
                                        songViewModel.onEvent(MusicPlayerUiEvents.NavigateTo("topTrack"))
                                        navController.navigate(Screen.TopTrack.route)
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(R.drawable.dottt),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.Top)
                                                .padding(horizontal = 5.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Top Tracks",
                                            fontFamily = ubuntu,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(horizontal = 5.dp)
                                        )
                                    },
                                )
                            }

                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            MusicPlayerNavGraph(
                                navController = navController,
                                viewModel = songViewModel
                            )

                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Music_Player_TaskTheme {

    }
}