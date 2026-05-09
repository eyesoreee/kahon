package com.example.kahon.feature_settings.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kahon.core.util.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentTheme by viewModel.appTheme.collectAsStateWithLifecycle()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var pendingRestoreUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pendingRestoreUri = uri
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = { Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Factory Reset") },
            text = { Text("This will permanently delete all rooms, storages, and items. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllData()
                        showDeleteConfirm = false
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (pendingRestoreUri != null) {
        AlertDialog(
            onDismissRequest = { pendingRestoreUri = null },
            icon = { Icon(Icons.Default.Restore, null) },
            title = { Text("Restore Backup") },
            text = { Text("This will replace all your current data with the data from the backup file. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        pendingRestoreUri?.let { viewModel.importData(it) }
                        pendingRestoreUri = null
                    }
                ) {
                    Text("Restore Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingRestoreUri = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Appearance",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            ThemeOption(
                title = "System Default",
                selected = currentTheme == AppTheme.SYSTEM,
                onClick = { viewModel.setTheme(AppTheme.SYSTEM) }
            )
            ThemeOption(
                title = "Light Mode",
                selected = currentTheme == AppTheme.LIGHT,
                onClick = { viewModel.setTheme(AppTheme.LIGHT) }
            )
            ThemeOption(
                title = "Dark Mode",
                selected = currentTheme == AppTheme.DARK,
                onClick = { viewModel.setTheme(AppTheme.DARK) }
            )

            Spacer(Modifier.height(16.dp))
            
            Text(
                "Data Management",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Surface(
                onClick = { viewModel.exportData() },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                ListItem(
                    headlineContent = { Text("Backup Data (JSON)") },
                    supportingContent = { Text("Export all your data to a file") },
                    leadingContent = { Icon(Icons.Default.Backup, null) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }

            Surface(
                onClick = { filePickerLauncher.launch("application/json") },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                ListItem(
                    headlineContent = { Text("Restore Data (JSON)") },
                    supportingContent = { Text("Import data from a backup file") },
                    leadingContent = { Icon(Icons.Default.Restore, null) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }

            Surface(
                onClick = { showDeleteConfirm = true },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
            ) {
                ListItem(
                    headlineContent = { Text("Clear All Data", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Delete everything and start fresh") },
                    leadingContent = { Icon(Icons.Default.DeleteForever, null, tint = MaterialTheme.colorScheme.error) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }

            Spacer(Modifier.height(24.dp))
            
            Text(
                "About",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ListItem(
                headlineContent = { Text("Kahon", fontWeight = FontWeight.Bold) },
                supportingContent = { Text("Version 1.0.0") },
                leadingContent = { Icon(Icons.Default.Info, null) },
                overlineContent = { Text("Application Info") }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            Text(
                text = "Your home, perfectly packed.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ThemeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
            if (selected) {
                Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
