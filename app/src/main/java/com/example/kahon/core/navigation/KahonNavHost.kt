package com.example.kahon.core.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.kahon.feature_item.presentation.ItemRoot
import com.example.kahon.feature_room.presentation.RoomRoot
import com.example.kahon.feature_storage.presentation.StorageRoot

@Composable
fun KahonNavHost(navController: NavHostController = rememberNavController()) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = RoomRoute,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearOutSlowInEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearOutSlowInEasing))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearOutSlowInEasing))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearOutSlowInEasing))
            }
        ) {
            composable<RoomRoute> {
                RoomRoot(
                    onNavigateToStorage = { roomId, roomName ->
                        navController.navigate(StorageRoute(roomId.toString(), roomName))
                    }
                )
            }

            composable<StorageRoute> {
                StorageRoot(
                    onBackClick = { navController.popBackStack() },
                    onNavigateToItems = { storageName, roomName ->
                        navController.navigate(
                            ItemRoute(
                                roomName = roomName,
                                storageName = storageName
                            )
                        )
                    }
                )
            }

            composable<ItemRoute>(
                deepLinks = listOf(
                    navDeepLink<ItemRoute>(basePath = "kahon://item")
                )
            ) {
                ItemRoot(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}