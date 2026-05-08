package com.example.kahon.feature_item.presentation

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.kahon.core.navigation.ItemRoute
import com.example.kahon.feature_item.data.local.Item
import com.example.kahon.feature_item.domain.repository.ItemRepository
import com.example.kahon.feature_room.domain.repository.RoomRepository
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle,
    private val storageRepository: StorageRepository,
    private val roomRepository: RoomRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val routeData = savedStateHandle.toRoute<ItemRoute>()
    val roomName = routeData.roomName
    val storageName = routeData.storageName

    private var storageId: Long? = null

    val uiState: StateFlow<ItemUiState> = flow {
        if (!roomRepository.doesRoomExist(roomName)) {
            emit(ItemUiState.Error("Room '$roomName' not found."))
            return@flow
        }

        val sId = storageRepository.getStorageId(roomName, storageName)
        if (sId == null) {
            emit(ItemUiState.Error("Storage '$storageName' not found."))
            return@flow
        }

        storageId = sId
        emitAll(
            combine(
                itemRepository.getItems(sId),
                itemRepository.getAllCategories()
            ) { items, categoriesFromDb ->
                val allCategories = (listOf(
                    "Electronics", "Clothing", "Kitchen", "Tools", "Office"
                ) + categoriesFromDb).distinct().sorted()

                ItemUiState.Ready(
                    state = ItemState(
                        storageName = storageName,
                        roomName = roomName,
                        items = items,
                        categories = allCategories
                    )
                )
            }
        )
    }
        .catch { e -> emit(ItemUiState.Error(e.localizedMessage)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ItemUiState.Loading)


    fun onAction(action: ItemAction) {
        when (action) {
            is ItemAction.AddItem -> onAddItem(
                action.name,
                action.category,
                action.quantity,
                action.imagePath
            )

            is ItemAction.UpdateItem -> onUpdateItem(action.item)
            is ItemAction.DeleteItem -> onDeleteItem(action.item)
            is ItemAction.DeleteCategory -> onDeleteCategory(action.category)
            else -> Unit
        }
    }

    private fun onAddItem(name: String, category: String, quantity: Int, imagePath: String?) {
        val sId = storageId ?: return
        viewModelScope.launch {
            val savedImagePath = imagePath?.let { saveImageToInternalStorage(it) }
            itemRepository.insertItem(
                Item(
                    name = name,
                    category = category,
                    storageId = sId,
                    quantity = quantity,
                    imagePath = savedImagePath
                )
            )
        }
    }

    private fun onUpdateItem(item: Item) {
        viewModelScope.launch {
            val finalItem = if (item.imagePath?.startsWith("content://") == true) {
                val savedPath = saveImageToInternalStorage(item.imagePath)
                item.copy(imagePath = savedPath)
            } else {
                item
            }
            itemRepository.updateItem(finalItem)
        }
    }

    private fun onDeleteItem(item: Item) {
        viewModelScope.launch {
            itemRepository.deleteItem(item.id)
        }
    }

    private fun onDeleteCategory(category: String) {
        viewModelScope.launch {
            itemRepository.clearCategory(category)
        }
    }

    private suspend fun saveImageToInternalStorage(uriString: String): String? {
        if (!uriString.startsWith("content://")) return uriString
        return withContext(Dispatchers.IO) {
            try {
                val uri = uriString.toUri()
                val inputStream = application.contentResolver.openInputStream(uri)
                val fileName = "item_${System.currentTimeMillis()}.jpg"
                val file = File(application.filesDir, fileName)
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
