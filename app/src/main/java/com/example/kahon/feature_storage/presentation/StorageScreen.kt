package com.example.kahon.feature_storage.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kahon.core.ui.KahonCardPalettes
import com.example.kahon.core.ui.toCardPalette
import com.example.kahon.feature_storage.domain.model.StorageWithCount

private val cardPalettes = KahonCardPalettes

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
    var isAddStorageDialogOpen by remember { mutableStateOf(false) }
    var newStorageName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableLongStateOf(KahonCardPalettes.first().gradientStart.value.toLong()) }

    var isStorageOptionsDialogOpen by remember { mutableStateOf(false) }
    var selectedStorage by remember { mutableStateOf<StorageWithCount?>(null) }

    var isEditStorageDialogOpen by remember { mutableStateOf(false) }
    var editStorageName by remember { mutableStateOf("") }

    if (isAddStorageDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                isAddStorageDialogOpen = false
                newStorageName = ""
            },
            title = { Text(text = "Add New Storage") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = newStorageName,
                        onValueChange = { newStorageName = it },
                        label = { Text("Storage Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Select Color", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(KahonCardPalettes) { palette ->
                            val colorValue = palette.gradientStart.value.toLong()
                            Surface(
                                onClick = { selectedColor = colorValue },
                                shape = CircleShape,
                                color = palette.gradientStart,
                                modifier = Modifier.size(40.dp),
                                border = if (selectedColor == colorValue) BorderStroke(
                                    2.dp,
                                    MaterialTheme.colorScheme.outline
                                ) else null
                            ) {}
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAction(StorageAction.OnConfirmAddStorage(newStorageName, selectedColor))
                        isAddStorageDialogOpen = false
                        newStorageName = ""
                    },
                    enabled = newStorageName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isAddStorageDialogOpen = false
                    newStorageName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (isStorageOptionsDialogOpen && selectedStorage != null) {
        AlertDialog(
            onDismissRequest = {
                isStorageOptionsDialogOpen = false
                selectedStorage = null
            },
            title = { Text(text = "Storage Options") },
            text = {
                Text(text = "What would you like to do with \"${selectedStorage?.name}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        editStorageName = selectedStorage?.name ?: ""
                        isStorageOptionsDialogOpen = false
                        isEditStorageDialogOpen = true
                    }
                ) {
                    Text("Edit Name")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedStorage?.let {
                            onAction(StorageAction.OnDeleteStorage(it.id))
                        }
                        isStorageOptionsDialogOpen = false
                        selectedStorage = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    if (isEditStorageDialogOpen && selectedStorage != null) {
        AlertDialog(
            onDismissRequest = {
                isEditStorageDialogOpen = false
                editStorageName = ""
                selectedStorage = null
            },
            title = { Text(text = "Edit Storage") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = editStorageName,
                        onValueChange = { editStorageName = it },
                        label = { Text("Storage Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Select Color", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(KahonCardPalettes) { palette ->
                            val colorValue = palette.gradientStart.value.toLong()
                            Surface(
                                onClick = { selectedColor = colorValue },
                                shape = CircleShape,
                                color = palette.gradientStart,
                                modifier = Modifier.size(40.dp),
                                border = if (selectedColor == colorValue) BorderStroke(
                                    2.dp,
                                    MaterialTheme.colorScheme.outline
                                ) else null
                            ) {}
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedStorage?.let {
                            onAction(
                                StorageAction.OnConfirmEditStorage(
                                    it.id,
                                    editStorageName,
                                    selectedColor
                                )
                            )
                        }
                        isEditStorageDialogOpen = false
                        editStorageName = ""
                        selectedStorage = null
                    },
                    enabled = editStorageName.isNotBlank()
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isEditStorageDialogOpen = false
                    editStorageName = ""
                    selectedStorage = null
                }) {
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
                onClick = { isAddStorageDialogOpen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Storage")
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
                        StorageCard(
                            name = storage.name,
                            itemCount = storage.itemCount,
                            palette = storage.color.toCardPalette(),
                            icon = Icons.Outlined.Inventory2,
                            onClick = {
                                onStorageClick(storage.name, roomName)
                            },
                            onLongClick = {
                                selectedStorage = storage
                                editStorageName = storage.name
                                selectedColor = storage.color
                                isStorageOptionsDialogOpen = true
                            }
                        )
                    }

                    item {
                        AddStorageCard(
                            onClick = { isAddStorageDialogOpen = true }
                        )
                    }

                    item {
                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
