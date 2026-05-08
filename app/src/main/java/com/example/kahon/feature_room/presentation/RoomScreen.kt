package com.example.kahon.feature_room.presentation

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kahon.core.ui.KahonCardPalettes
import com.example.kahon.core.ui.KahonRoomIcons
import com.example.kahon.core.ui.toCardPalette
import com.example.kahon.core.ui.toRoomIcon
import com.example.kahon.feature_room.domain.model.RoomWithCount
import kotlinx.coroutines.launch

@Composable
fun RoomRoot(
    viewModel: RoomViewModel = hiltViewModel(),
    onNavigateToStorage: (Long, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RoomScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        onRoomClick = onNavigateToStorage
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    uiState: RoomUiState,
    onAction: (RoomAction) -> Unit,
    onRoomClick: (Long, String) -> Unit,
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()

    var isAddRoomDialogOpen by remember { mutableStateOf(false) }
    var newRoomName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableLongStateOf(KahonCardPalettes.first().gradientStart.value.toLong()) }
    var selectedIconKey by remember { mutableStateOf(KahonRoomIcons.keys.first()) }

    var isRoomOptionsDialogOpen by remember { mutableStateOf(false) }
    var isDeleteWarningDialogOpen by remember { mutableStateOf(false) }
    var selectedRoom by remember { mutableStateOf<RoomWithCount?>(null) }

    var isEditRoomDialogOpen by remember { mutableStateOf(false) }
    var editRoomName by remember { mutableStateOf("") }

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
            placeholder = {
                Text(
                    modifier = Modifier.clearAndSetSemantics {},
                    text = "Search rooms…",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
        )
    }

    if (isAddRoomDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                isAddRoomDialogOpen = false
                newRoomName = ""
            },
            title = { Text(text = "Add New Room") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = newRoomName,
                        onValueChange = { newRoomName = it },
                        label = { Text("Room Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Select Icon", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(KahonRoomIcons.keys.toList(), key = { it }) { key ->
                            val icon = KahonRoomIcons[key]!!
                            Surface(
                                onClick = { selectedIconKey = key },
                                shape = CircleShape,
                                color = if (selectedIconKey == key) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = if (selectedIconKey == key) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Text("Select Color", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(KahonCardPalettes, key = { it.gradientStart.value.toLong() }) { palette ->
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
                        onAction(
                            RoomAction.OnConfirmAddRoom(
                                newRoomName,
                                selectedColor,
                                selectedIconKey
                            )
                        )
                        isAddRoomDialogOpen = false
                        newRoomName = ""
                    },
                    enabled = newRoomName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isAddRoomDialogOpen = false
                    newRoomName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (isRoomOptionsDialogOpen && selectedRoom != null) {
        AlertDialog(
            onDismissRequest = {
                isRoomOptionsDialogOpen = false
                selectedRoom = null
            },
            title = { Text(text = "Room Options") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "What would you like to do with \"${selectedRoom?.name}\"?")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        editRoomName = selectedRoom?.name ?: ""
                        isRoomOptionsDialogOpen = false
                        isEditRoomDialogOpen = true
                    }
                ) {
                    Text("Edit Name")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if ((selectedRoom?.storageCount ?: 0) > 0) {
                            isDeleteWarningDialogOpen = true
                            isRoomOptionsDialogOpen = false
                        } else {
                            selectedRoom?.let { onAction(RoomAction.OnDeleteRoom(it.id)) }
                            isRoomOptionsDialogOpen = false
                            selectedRoom = null
                        }
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    if (isDeleteWarningDialogOpen && selectedRoom != null) {
        AlertDialog(
            onDismissRequest = {
                isDeleteWarningDialogOpen = false
                selectedRoom = null
            },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text(text = "Delete Room?") },
            text = {
                Text(
                    text = "This room contains ${selectedRoom?.storageCount} storages. Deleting it will permanently remove everything inside. This cannot be undone."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedRoom?.let { onAction(RoomAction.OnDeleteRoom(it.id)) }
                        isDeleteWarningDialogOpen = false
                        selectedRoom = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDeleteWarningDialogOpen = false
                    selectedRoom = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (isEditRoomDialogOpen && selectedRoom != null) {
        AlertDialog(
            onDismissRequest = {
                isEditRoomDialogOpen = false
                editRoomName = ""
                selectedRoom = null
            },
            title = { Text(text = "Edit Room") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = editRoomName,
                        onValueChange = { editRoomName = it },
                        label = { Text("Room Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Select Icon", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(KahonRoomIcons.keys.toList(), key = { it }) { key ->
                            val icon = KahonRoomIcons[key]!!
                            Surface(
                                onClick = { selectedIconKey = key },
                                shape = CircleShape,
                                color = if (selectedIconKey == key) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = if (selectedIconKey == key) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Text("Select Color", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(KahonCardPalettes, key = { it.gradientStart.value.toLong() }) { palette ->
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
                        selectedRoom?.let {
                            onAction(
                                RoomAction.OnConfirmEditRoom(
                                    it.id,
                                    editRoomName,
                                    selectedColor,
                                    selectedIconKey
                                )
                            )
                        }
                        isEditRoomDialogOpen = false
                        editRoomName = ""
                        selectedRoom = null
                    },
                    enabled = editRoomName.isNotBlank()
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isEditRoomDialogOpen = false
                    editRoomName = ""
                    selectedRoom = null
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
                onClick = { isAddRoomDialogOpen = true },
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
                val filteredRooms by remember(uiState.roomState.rooms, textFieldState.text) {
                    derivedStateOf {
                        val query = textFieldState.text.toString()
                        if (query.isBlank()) {
                            uiState.roomState.rooms
                        } else {
                            uiState.roomState.rooms.filter {
                                it.name.contains(query, ignoreCase = true)
                            }
                        }
                    }
                }
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
                        items(filteredRooms, key = { it.id }) { room ->
                            RoomCard(
                                name = room.name,
                                storageCount = room.storageCount,
                                palette = room.color.toCardPalette(),
                                icon = room.icon.toRoomIcon(),
                                onClick = {
                                    onRoomClick(room.id, room.name)
                                },
                                onLongClick = {
                                    selectedRoom = room
                                    editRoomName = room.name
                                    selectedColor = room.color
                                    selectedIconKey = room.icon
                                    isRoomOptionsDialogOpen = true
                                }
                            )
                        }

                        item {
                            AddRoomCard(
                                onClick = { isAddRoomDialogOpen = true }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
