package com.example.kahon.feature_location.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kahon.feature_location.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Loading)
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        loadLocations()
    }
    fun onAction(action: LocationAction) {
//        when (action) {
//        }
    }

    fun loadLocations() {
        _uiState.value = LocationUiState.Loading
        viewModelScope.launch {
            try {
                val locations = locationRepository.getLocations()
                _uiState.value = LocationUiState.Ready(
                    locationState = LocationState(
                        locations = locations
                    )
                )
            } catch (e: Exception) {
                _uiState.value = LocationUiState.Error(e.message)
            }
        }
    }
}