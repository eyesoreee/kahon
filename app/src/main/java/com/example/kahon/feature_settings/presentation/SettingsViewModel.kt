package com.example.kahon.feature_settings.presentation

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kahon.core.util.AppTheme
import com.example.kahon.core.util.DataBackup
import com.example.kahon.core.util.ThemePreferences
import com.example.kahon.feature_item.domain.repository.ItemRepository
import com.example.kahon.feature_room.domain.repository.RoomRepository
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val roomRepository: RoomRepository,
    private val storageRepository: StorageRepository,
    private val itemRepository: ItemRepository,
    private val themePreferences: ThemePreferences
) : ViewModel() {

    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    val appTheme: StateFlow<AppTheme> = themePreferences.theme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.SYSTEM)

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreferences.setTheme(theme)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            roomRepository.deleteAllRooms()
            Toast.makeText(application, "All data cleared", Toast.LENGTH_SHORT).show()
        }
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                val rooms = roomRepository.getAllRoomsRaw()
                val storages = storageRepository.getAllStoragesRaw()
                val items = itemRepository.getAllItemsRaw()
                
                val backup = DataBackup(rooms, storages, items)
                val jsonString = json.encodeToString(backup)
                
                val file = File(application.cacheDir, "kahon_backup.json")
                file.writeText(jsonString)
                
                val uri = FileProvider.getUriForFile(
                    application,
                    "${application.packageName}.provider",
                    file
                )
                
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/json"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                
                application.startActivity(Intent.createChooser(intent, "Export Backup").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            } catch (e: Exception) {
                Toast.makeText(application, "Export failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun importData(uri: Uri) {
        viewModelScope.launch {
            try {
                val jsonString = application.contentResolver.openInputStream(uri)?.use {
                    it.bufferedReader().readText()
                } ?: throw Exception("Could not read file")

                val backup = json.decodeFromString<DataBackup>(jsonString)

                roomRepository.deleteAllRooms()
                roomRepository.insertRooms(backup.rooms)
                storageRepository.insertStorages(backup.storages)
                itemRepository.insertItems(backup.items)
                
                Toast.makeText(application, "Data restored successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(application, "Import failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}
