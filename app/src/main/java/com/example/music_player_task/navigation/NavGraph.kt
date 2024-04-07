package com.example.music_player_task.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.music_player_task.songs.presentation.music_player_screens.ForYouScreen
import com.example.music_player_task.songs.presentation.music_player_screens.SongScreen
import com.example.music_player_task.songs.presentation.music_player_screens.TopTrack
import com.example.music_player_task.songs.presentation.viewModels.SongViewModel

@Composable
fun MusicPlayerNavGraph(
    viewModel: SongViewModel,
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.ForYou.route) {

        composable(route = Screen.ForYou.route) {
            ForYouScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.SongScreen.route,
            arguments = listOf(
                navArgument("imageId") { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val imageKey = navBackStackEntry.arguments?.getString("imageId")
            if (imageKey != null) {
                SongScreen(
                    navController = navController,
                    imageId = imageKey,
                    viewModel = viewModel
                )
            }
        }

        composable(route = Screen.TopTrack.route) {
            TopTrack(navController = navController, viewModel = viewModel)
        }

    }
}

