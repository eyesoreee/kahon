package com.example.kahon.feature_location.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LocationRoot(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocationScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun LocationScreen(
    uiState: LocationUiState,
    onAction: (LocationAction) -> Unit,
) {
    when (uiState) {
        is LocationUiState.Loading -> {}
        is LocationUiState.Ready -> {}
        is LocationUiState.Error -> {}
    }
}