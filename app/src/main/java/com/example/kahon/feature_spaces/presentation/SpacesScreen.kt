package com.example.kahon.feature_spaces.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SpacesRoot(
    viewModel: SpacesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SpacesScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun SpacesScreen(
    uiState: SpacesUiState,
    onAction: (SpacesAction) -> Unit,
) {
    when (uiState) {
        is SpacesUiState.Loading -> {}
        is SpacesUiState.Ready -> {}
        is SpacesUiState.Error -> {}
    }
}