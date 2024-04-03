package com.example.music_player_task.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.androidjetpackcomposepracticeprojects.store.presentation.viewModels.StoreProductDetailsViewModel

@Composable
fun MusicPlayerNavGraph(
    viewModel: StoreProductDetailsViewModel,
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.ForYou.route) {

//        composable(route = StoreScreen.StoreHomeScreen.route) {
//           // StoreProductScreen(navController = navController, productViewModel = viewModel)
//        }
//
//        composable(
//            route = StoreScreen.StoreProductDetails.route,
//            arguments = listOf(
//                navArgument("productIndex") { type = NavType.StringType },
//            )
//        ) { navBackStackEntry ->
//            val productIndex = navBackStackEntry.arguments?.getString("productIndex")
//            if (productIndex != null) {
//                StoreProductDetails(
//                    navController = navController,
//                    index = productIndex,
//                    viewModel = viewModel
//                )
//            }
//        }
//
//        composable(route = StoreScreen.StoreProfileScreen.route) {
//            //StoreProfileScreen(navController = navController)
//        }
//
//        composable(route = StoreScreen.StoreProductCart.route) {
//           // ProductCart(navController = navController, viewModel = viewModel)
//        }
    }
}

