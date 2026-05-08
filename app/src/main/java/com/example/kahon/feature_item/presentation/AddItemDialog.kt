package com.example.kahon.feature_item.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    item: com.example.kahon.feature_item.data.local.Item? = null,
    categories: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, category: String, quantity: Int, imagePath: String?) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var name by remember(item) { mutableStateOf(item?.name ?: "") }
    var quantity by remember(item) { mutableStateOf(item?.quantity ?: 1) }
    var selectedCategory by remember(item) { mutableStateOf(item?.category ?: "") }
    var imagePath by remember(item) { mutableStateOf(item?.imagePath) }
    var customCategory by remember { mutableStateOf("") }
    var isAddingCustomCategory by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imagePath = uri.toString()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            imagePath = tempCameraUri.toString()
        }
    }

    fun launchCamera() {
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
        tempCameraUri = uri
        cameraLauncher.launch(uri)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .padding(24.dp)
            .imePadding(),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (item == null) "Add New Item" else "Edit Item",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (imagePath != null) {
                        AsyncImage(
                            model = imagePath,
                            contentDescription = "Item Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = { launchCamera() }) {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    "Camera",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            IconButton(onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }) {
                                Icon(
                                    Icons.Default.PhotoLibrary,
                                    "Gallery",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            IconButton(onClick = { imagePath = null }) {
                                Icon(
                                    Icons.Default.Delete,
                                    "Remove",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { launchCamera() }
                                    .padding(16.dp)
                            ) {
                                Icon(Icons.Default.AddAPhoto, null, Modifier.size(32.dp))
                                Text("Camera", style = MaterialTheme.typography.labelMedium)
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .padding(16.dp)
                            ) {
                                Icon(Icons.Default.PhotoLibrary, null, Modifier.size(32.dp))
                                Text("Gallery", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    placeholder = { Text("e.g. Winter Jacket") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Quantity",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("-", style = MaterialTheme.typography.headlineMedium)
                        }

                        OutlinedTextField(
                            value = quantity.toString(),
                            onValueChange = { newValue ->
                                newValue.filter { it.isDigit() }.toIntOrNull()
                                    ?.let { quantity = it }
                            },
                            modifier = Modifier.width(80.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("+", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Category",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = {
                                    selectedCategory = category
                                    isAddingCustomCategory = false
                                },
                                label = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(category)
                                        if (selectedCategory == category && category != "Uncategorized") {
                                            Spacer(Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Delete Category",
                                                modifier = Modifier
                                                    .clickable {
                                                        onDeleteCategory(category)
                                                        selectedCategory = ""
                                                    }
                                                    .size(14.dp)
                                            )
                                        }
                                    }
                                },
                                leadingIcon = if (selectedCategory == category) {
                                    { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                                } else null,
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }

                        FilterChip(
                            selected = isAddingCustomCategory,
                            onClick = {
                                isAddingCustomCategory = true
                                selectedCategory = ""
                            },
                            label = { Text("+ New") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    if (isAddingCustomCategory) {
                        OutlinedTextField(
                            value = customCategory,
                            onValueChange = { customCategory = it },
                            placeholder = { Text("Enter category name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                if (customCategory.isNotBlank()) {
                                    IconButton(onClick = { customCategory = "" }) {
                                        Icon(Icons.Default.Close, null)
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val category = if (isAddingCustomCategory) customCategory else selectedCategory
                    onConfirm(name, category.ifBlank { "Uncategorized" }, quantity, imagePath)
                },
                enabled = name.isNotBlank(),
                shape = RoundedCornerShape(50)
            ) {
                Text(if (item == null) "Add Item" else "Update Item")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
