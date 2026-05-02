package com.example.kahon.core.navigation

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.kahon.feature_item.presentation.ItemRoot
import com.example.kahon.feature_room.presentation.RoomRoot
import com.example.kahon.feature_storage.presentation.StorageRoot

@Composable
fun KahonNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Handle deep links when the app is already running (singleTop)
    DisposableEffect(context) {
        val activity = context as? ComponentActivity
        val listener = Consumer<Intent> { intent ->
            val data = intent.data
            if (data != null) {
                Log.d("KahonNav", "Processing deep link: $data")
                val handled = navController.handleDeepLink(intent)
                Log.d("KahonNav", "Deep link handled: $handled")
            }
        }
        activity?.addOnNewIntentListener(listener)
        onDispose {
            activity?.removeOnNewIntentListener(listener)
        }
    }

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
                        navController.navigate(ItemRoute(roomName = roomName, storageName = storageName))
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