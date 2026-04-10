package com.example.kahon.feature_location.presentation

import com.example.kahon.feature_location.data.local.Location

data class LocationState(
    val locations: List<Location> = emptyList(),
    val searchQuery: String = ""
)