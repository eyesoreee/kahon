package com.example.kahon.feature_storage.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StorageRoot(
    viewModel: StorageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun StorageScreen(
    uiState: StorageUiState,
    onAction: (StorageAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when (uiState) {
            is StorageUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is StorageUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage ?: "An unknown error occurred")
                }
            }

            is StorageUiState.Ready -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("HELLO")
                }
            }
        }
    }
}