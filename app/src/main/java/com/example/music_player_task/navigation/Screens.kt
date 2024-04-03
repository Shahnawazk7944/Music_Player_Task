package com.example.music_player_task.navigation

sealed class Screen(val route: String) {
    data object ForYou : Screen(route = "forYou")
    data object TopTrack : Screen(route = "topTrack")

    data object SongScreen : Screen(
        route = "songScreen/{imageId}"
    ) {
        fun passToSongScreen(
            imageId: String,
        ): String {
            return "songScreen/$imageId"
        }
    }
}