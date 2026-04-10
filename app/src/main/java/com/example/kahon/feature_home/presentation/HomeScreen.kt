package com.example.kahon.feature_home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SpacesRoot(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SpacesScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun SpacesScreen(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit,
) {
    when (uiState) {
        is HomeUiState.Loading -> {}
        is HomeUiState.Ready -> {}
        is HomeUiState.Error -> {}
    }
}