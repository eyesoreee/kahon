package com.example.kahon.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kahon.feature_item.presentation.ItemRoot
import com.example.kahon.feature_room.presentation.RoomRoot
import com.example.kahon.feature_storage.presentation.StorageRoot

@Composable
fun KahonNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RoomRoute
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
                    navController.navigate(ItemRoute(storageName, roomName))
                }
            )
        }

        composable<ItemRoute> {
            ItemRoot()
        }
    }
}