package com.example.kahon.feature_room.presentation

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Weekend
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

private val cardPalettes = listOf(
    CardPalette(Color(0xFFFF8C42), Color(0xFFFFBF69), Color(0xFF7A3B1E), Color(0xFF3D1A00)),
    CardPalette(Color(0xFF6DBF9E), Color(0xFFAAE4CA), Color(0xFF1A5C3F), Color(0xFF0D3323)),
    CardPalette(Color(0xFF7B9EE0), Color(0xFFB5CEFD), Color(0xFF1C3B7A), Color(0xFF0D1F45)),
    CardPalette(Color(0xFFE07BA8), Color(0xFFFDB5D0), Color(0xFF7A1C45), Color(0xFF45001F)),
    CardPalette(Color(0xFFB47FE0), Color(0xFFD9B5FD), Color(0xFF4A1C7A), Color(0xFF270D45)),
    CardPalette(Color(0xFFE0C47B), Color(0xFFFDE9B5), Color(0xFF7A5A1C), Color(0xFF45320D)),
)

data class CardPalette(
    val gradientStart: Color,
    val gradientEnd: Color,
    val iconTint: Color,
    val textTint: Color,
)

private val roomIcons: List<ImageVector> = listOf(
    Icons.Outlined.Weekend,
    Icons.Outlined.Bed,
    Icons.Outlined.Kitchen,
    Icons.Outlined.Checkroom,
    Icons.Outlined.Chair,
    Icons.Outlined.Storage,
)

@Composable
fun RoomRoot(
    viewModel: RoomViewModel = hiltViewModel(),
    onNavigateToContainers: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RoomScreen(uiState = uiState, onAction = viewModel::onAction)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    uiState: RoomUiState,
    onAction: (RoomAction) -> Unit,
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
            placeholder = {
                Text(
                    modifier = Modifier.clearAndSetSemantics {},
                    text = "Search rooms or boxes…",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
        )
    }

    if (uiState is RoomUiState.Ready && uiState.roomState.isAddRoomDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(RoomAction.OnDismissAddRoomDialog) },
            title = { Text(text = "Add New Room") },
            text = {
                OutlinedTextField(
                    value = uiState.roomState.newRoomName,
                    onValueChange = { onAction(RoomAction.OnNewRoomNameChange(it)) },
                    label = { Text("Room Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { onAction(RoomAction.OnConfirmAddRoom) },
                    enabled = uiState.roomState.newRoomName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(RoomAction.OnDismissAddRoomDialog) }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState is RoomUiState.Ready && uiState.roomState.isRoomOptionsDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(RoomAction.OnDismissRoomOptions) },
            title = { Text(text = "Room Options") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val selectedRoom =
                        uiState.roomState.rooms.find { it.id == uiState.roomState.selectedRoomId }
                    Text(text = "What would you like to do with \"${selectedRoom?.name ?: ""}\"?")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        uiState.roomState.selectedRoomId?.let {
                            onAction(RoomAction.OnEditRoomClick(it))
                        }
                    }
                ) {
                    Text("Edit Name")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        uiState.roomState.selectedRoomId?.let {
                            onAction(RoomAction.OnDeleteRoomClick(it))
                        }
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    if (uiState is RoomUiState.Ready && uiState.roomState.isEditRoomDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(RoomAction.OnDismissEditRoomDialog) },
            title = { Text(text = "Edit Room Name") },
            text = {
                OutlinedTextField(
                    value = uiState.roomState.editRoomName,
                    onValueChange = { onAction(RoomAction.OnEditRoomNameChange(it)) },
                    label = { Text("Room Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { onAction(RoomAction.OnConfirmEditRoom) },
                    enabled = uiState.roomState.editRoomName.isNotBlank()
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(RoomAction.OnDismissEditRoomDialog) }) {
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
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = "kahon.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp,
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Your home, perfectly packed.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Surface(
                        onClick = {},
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(RoomAction.OnAddRoomClick) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Room")
            }
        }
    ) { innerPadding ->
        when (uiState) {
            is RoomUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RoomUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage ?: "An unknown error occurred")
                }
            }

            is RoomUiState.Ready -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))

                    SearchBar(state = searchBarState, inputField = inputField)

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Your Rooms",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                ),
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = "${uiState.roomState.rooms.size} rooms",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.roomState.rooms) { room ->
                            val index = uiState.roomState.rooms.indexOf(room)
                            RoomCard(
                                name = room.name,
                                storageCount = room.storageCount,
                                palette = cardPalettes[index % cardPalettes.size],
                                icon = roomIcons[index % roomIcons.size],
                                onClick = {
//                                    onAction(
//                                        RoomAction.OnRoomClick(room.id, room.name)
//                                    )
                                },
                                onLongClick = {
                                    onAction(RoomAction.OnRoomLongClick(room.id))
                                }
                            )
                        }

                        item {
                            AddRoomCard(
                                onClick = { onAction(RoomAction.OnAddRoomClick) }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
