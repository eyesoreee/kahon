package com.example.kahon.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kahon.feature_location.presentation.LocationRoot

@Composable
fun KahonNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LocationsRoute
    ) {

        composable<LocationsRoute> {
            LocationRoot(
                onNavigateToContainers = { locationId, locationName ->
                    navController.navigate(ContainersRoute(locationId, locationName))
                }
            )
        }
    }
}