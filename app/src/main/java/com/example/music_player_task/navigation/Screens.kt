package com.example.music_player_task.navigation

sealed class Screen(val route: String) {
    data object StoreHomeScreen : Screen(route = "storeHomeScreen")
    data object StoreProfileScreen : Screen(route = "storeProfileScreen")
    data object StoreProductCart : Screen(route = "storeProductCart")
    data object StoreProductDetails : Screen(
        route = "storeProductDetails/{productIndex}"
    ) {
        fun passToProductDetailsScree(
            productIndex: Int,
        ): String {
            return "storeProductDetails/$productIndex"
        }
    }
}