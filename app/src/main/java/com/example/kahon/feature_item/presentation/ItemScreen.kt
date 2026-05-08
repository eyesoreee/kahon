package com.example.kahon.feature_item.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kahon.core.ui.KahonCardPalettes
import com.example.kahon.core.util.QrCodeGenerator
import com.example.kahon.core.util.ShareUtils
import com.example.kahon.feature_item.data.local.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ItemRoot(
    viewModel: ItemViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ItemScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    uiState: ItemUiState,
    onAction: (ItemAction) -> Unit,
    onBackClick: () -> Unit
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var isAddingItem by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<Item?>(null) }
    var itemToDelete by remember { mutableStateOf<Item?>(null) }

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
            placeholder = {
                Text(
                    modifier = Modifier.clearAndSetSemantics {},
                    text = "Search items…",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
        )
    }

    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text(text = "Delete Item?") },
            text = { Text("Are you sure you want to delete \"${itemToDelete?.name}\"? This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDelete?.let { onAction(ItemAction.DeleteItem(it)) }
                        itemToDelete = null
                        editingItem = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
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
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = {
                    if (uiState is ItemUiState.Ready) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "${uiState.state.roomName} / ${uiState.state.storageName}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is ItemUiState.Ready) {
                FloatingActionButton(
                    onClick = { isAddingItem = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Item")
                }
            }
        }
    ) { innerPadding ->
        when (uiState) {
            is ItemUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ItemUiState.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = uiState.message ?: "An unknown error occurred",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = onBackClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text("Go Back")
                        }
                    }
                }
            }

            is ItemUiState.Ready -> {
                val context = LocalContext.current
                val filteredItems by remember(
                    uiState.state.items,
                    textFieldState.text,
                    selectedCategory
                ) {
                    derivedStateOf {
                        val query = textFieldState.text.toString()
                        uiState.state.items.filter { item ->
                            val matchesSearch = item.name.contains(query, ignoreCase = true)
                            val matchesCategory =
                                selectedCategory == null || item.category == selectedCategory
                            matchesSearch && matchesCategory
                        }
                    }
                }
                val scope = rememberCoroutineScope()
                val deepLinkUrl = remember(uiState.state.roomName, uiState.state.storageName) {
                    val encodedRoom = Uri.encode(uiState.state.roomName)
                    val encodedStorage = Uri.encode(uiState.state.storageName)
                    "kahon://item?roomName=$encodedRoom&storageName=$encodedStorage"
                }

                val qrOnColor = MaterialTheme.colorScheme.primary.toArgb()
                val qrOffColor = Color.Transparent.toArgb()

                val qrBitmap by produceState<Bitmap?>(initialValue = null, deepLinkUrl) {
                    value = withContext(Dispatchers.Default) {
                        QrCodeGenerator.generate(
                            content = deepLinkUrl,
                            onColor = qrOnColor,
                            offColor = qrOffColor
                        )
                    }
                }

                if (isAddingItem) {
                    AddItemDialog(
                        categories = uiState.state.categories,
                        onDismiss = { isAddingItem = false },
                        onConfirm = { name, category, quantity, imagePath ->
                            onAction(ItemAction.AddItem(name, category, quantity, imagePath))
                            isAddingItem = false
                        },
                        onDeleteCategory = { category ->
                            onAction(ItemAction.DeleteCategory(category))
                        }
                    )
                }

                editingItem?.let { item ->
                    AddItemDialog(
                        item = item,
                        categories = uiState.state.categories,
                        onDismiss = { editingItem = null },
                        onConfirm = { name, category, quantity, imagePath ->
                            onAction(
                                ItemAction.UpdateItem(
                                    item.copy(
                                        name = name,
                                        category = category,
                                        quantity = quantity,
                                        imagePath = imagePath
                                    )
                                )
                            )
                            editingItem = null
                        },
                        onDeleteCategory = { category ->
                            onAction(ItemAction.DeleteCategory(category))
                        },
                        onDelete = {
                            itemToDelete = item
                        }
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.state.storageName,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = (-0.5).sp,
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "${uiState.state.items.size} items packed",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(24.dp))

                            Surface(
                                shape = RoundedCornerShape(32.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f),
                                shadowElevation = 4.dp,
                                tonalElevation = 2.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        qrBitmap?.let { bitmap ->
                                            Image(
                                                bitmap = bitmap.asImageBitmap(),
                                                contentDescription = "QR Code for ${uiState.state.storageName}",
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } ?: CircularProgressIndicator(
                                            modifier = Modifier.size(40.dp),
                                            strokeWidth = 2.dp
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Button(
                                            onClick = {
                                                scope.launch(Dispatchers.Default) {
                                                    val shareBitmap =
                                                        QrCodeGenerator.generate(deepLinkUrl)
                                                    withContext(Dispatchers.Main) {
                                                        ShareUtils.shareQrCode(
                                                            context,
                                                            shareBitmap,
                                                            uiState.state.storageName
                                                        )
                                                    }
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            shape = RoundedCornerShape(50)
                                        ) {
                                            Icon(Icons.Outlined.Share, null, Modifier.size(18.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                "Share",
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                scope.launch(Dispatchers.Default) {
                                                    val downloadBitmap =
                                                        QrCodeGenerator.generate(deepLinkUrl)
                                                    val success = ShareUtils.saveQrCodeToGallery(
                                                        context,
                                                        downloadBitmap,
                                                        uiState.state.storageName
                                                    )

                                                    withContext(Dispatchers.Main) {
                                                        Toast.makeText(
                                                            context,
                                                            if (success) "QR Code saved to Gallery" else "Failed to save QR Code",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            shape = RoundedCornerShape(50)
                                        ) {
                                            Icon(
                                                Icons.Outlined.Download,
                                                null,
                                                Modifier.size(18.dp)
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                "Save",
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(32.dp))

                            SearchBar(state = searchBarState, inputField = inputField)

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "Categories",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    FilterChip(
                                        selected = selectedCategory == null,
                                        onClick = { selectedCategory = null },
                                        label = { Text("All") },
                                        leadingIcon = if (selectedCategory == null) {
                                            {
                                                Icon(
                                                    Icons.Default.FilterList,
                                                    null,
                                                    Modifier.size(18.dp)
                                                )
                                            }
                                        } else null
                                    )
                                }
                                items(uiState.state.categories, key = { it }) { category ->
                                    FilterChip(
                                        selected = selectedCategory == category,
                                        onClick = {
                                            selectedCategory =
                                                if (selectedCategory == category) null else category
                                        },
                                        label = { Text(category) }
                                    )
                                }
                            }

                            Spacer(Modifier.height(24.dp))

                            Text(
                                text = if (selectedCategory == null) "Inside this storage (${filteredItems.size})" else "$selectedCategory (${filteredItems.size})",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }

                    if (filteredItems.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 64.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = if (textFieldState.text.isNotEmpty() || selectedCategory != null) "No matches found" else "Empty Storage",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (textFieldState.text.isNotEmpty() || selectedCategory != null) "Try a different search or filter." else "Tap the + button to add items.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        itemsIndexed(filteredItems, key = { _, item -> item.id }) { _, item ->
                            ItemCard(
                                item = item,
                                palette = KahonCardPalettes[(item.id % KahonCardPalettes.size).toInt()],
                                onClick = { editingItem = item }
                            )
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
