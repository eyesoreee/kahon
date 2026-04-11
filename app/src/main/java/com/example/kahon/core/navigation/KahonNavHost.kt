package com.example.kahon.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kahon.feature_room.presentation.RoomRoot

@Composable
fun KahonNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RoomsRoute
    ) {

        composable<RoomsRoute> {
            RoomRoot(
                onNavigateToContainers = { roomId, roomName ->
                    navController.navigate(ContainersRoute(roomId, roomName))
                }
            )
        }
    }
}