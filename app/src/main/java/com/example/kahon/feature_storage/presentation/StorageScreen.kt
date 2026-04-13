package com.example.kahon.feature_storage.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val cardPalettes = listOf(
    CardPalette(Color(0xFFFF8C42), Color(0xFFFFBF69), Color(0xFF7A3B1E), Color(0xFF3D1A00)),
    CardPalette(Color(0xFF6DBF9E), Color(0xFFAAE4CA), Color(0xFF1A5C3F), Color(0xFF0D3323)),
    CardPalette(Color(0xFF7B9EE0), Color(0xFFB5CEFD), Color(0xFF1C3B7A), Color(0xFF0D1F45)),
    CardPalette(Color(0xFFE07BA8), Color(0xFFFDB5D0), Color(0xFF7A1C45), Color(0xFF45001F)),
    CardPalette(Color(0xFFB47FE0), Color(0xFFD9B5FD), Color(0xFF4A1C7A), Color(0xFF270D45)),
    CardPalette(Color(0xFFE0C47B), Color(0xFFFDE9B5), Color(0xFF7A5A1C), Color(0xFF45320D)),
)

@Composable
fun StorageRoot(
    viewModel: StorageViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToItems: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageScreen(
        uiState = uiState,
        roomName = viewModel.roomName,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onStorageClick = onNavigateToItems
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreen(
    uiState: StorageUiState,
    roomName: String,
    onAction: (StorageAction) -> Unit,
    onBackClick: () -> Unit,
    onStorageClick: (String, String) -> Unit
) {
    if (uiState is StorageUiState.Ready && uiState.storageState.isAddStorageDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(StorageAction.OnDismissAddStorageDialog) },
            title = { Text(text = "Add New Box") },
            text = {
                OutlinedTextField(
                    value = uiState.storageState.newStorageName,
                    onValueChange = { onAction(StorageAction.OnNewStorageNameChange(it)) },
                    label = { Text("Box Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { onAction(StorageAction.OnConfirmAddStorage) },
                    enabled = uiState.storageState.newStorageName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(StorageAction.OnDismissAddStorageDialog) }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState is StorageUiState.Ready && uiState.storageState.isStorageOptionsDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(StorageAction.OnDismissStorageOptions) },
            title = { Text(text = "Box Options") },
            text = {
                val selectedStorage =
                    uiState.storageState.storages.find { it.id == uiState.storageState.selectedStorageId }
                Text(text = "What would you like to do with \"${selectedStorage?.name ?: ""}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        uiState.storageState.selectedStorageId?.let {
                            onAction(StorageAction.OnEditStorageClick(it))
                        }
                    }
                ) {
                    Text("Edit Name")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        uiState.storageState.selectedStorageId?.let {
                            onAction(StorageAction.OnDeleteStorageClick(it))
                        }
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    if (uiState is StorageUiState.Ready && uiState.storageState.isEditStorageDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(StorageAction.OnDismissEditStorageDialog) },
            title = { Text(text = "Edit Box Name") },
            text = {
                OutlinedTextField(
                    value = uiState.storageState.editStorageName,
                    onValueChange = { onAction(StorageAction.OnEditStorageNameChange(it)) },
                    label = { Text("Box Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { onAction(StorageAction.OnConfirmEditStorage) },
                    enabled = uiState.storageState.editStorageName.isNotBlank()
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(StorageAction.OnDismissEditStorageDialog) }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = roomName,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp,
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Manage your storages in this room",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(StorageAction.OnAddStorageClick) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Box")
            }
        }
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Your Storages",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                ),
                            )

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.primaryContainer,
                            ) {
                                Text(
                                    text = "${uiState.storageState.storages.size} storages",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    items(uiState.storageState.storages) { storage ->
                        val index = uiState.storageState.storages.indexOf(storage)
                        StorageCard(
                            name = storage.name,
                            itemCount = storage.itemCount,
                            palette = cardPalettes[index % cardPalettes.size],
                            icon = Icons.Outlined.Inventory2,
                            onClick = {
                                onStorageClick(storage.name, roomName)
                            },
                            onLongClick = {
                                onAction(StorageAction.OnStorageLongClick(storage.id))
                            }
                        )
                    }

                    item {
                        Spacer(Modifier.height(80.dp)) // Padding for FAB
                    }
                }
            }
        }
    }
}
