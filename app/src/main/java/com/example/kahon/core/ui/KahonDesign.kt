package com.example.kahon.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Weekend
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CardPalette(
    val gradientStart: Color,
    val gradientEnd: Color,
    val iconTint: Color,
    val textTint: Color,
)

val KahonCardPalettes = listOf(
    CardPalette(Color(0xFFFF8C42), Color(0xFFFFBF69), Color(0xFF7A3B1E), Color(0xFF3D1A00)),
    CardPalette(Color(0xFF6DBF9E), Color(0xFFAAE4CA), Color(0xFF1A5C3F), Color(0xFF0D3323)),
    CardPalette(Color(0xFF7B9EE0), Color(0xFFB5CEFD), Color(0xFF1C3B7A), Color(0xFF0D1F45)),
    CardPalette(Color(0xFFE07BA8), Color(0xFFFDB5D0), Color(0xFF7A1C45), Color(0xFF45001F)),
    CardPalette(Color(0xFFB47FE0), Color(0xFFD9B5FD), Color(0xFF4A1C7A), Color(0xFF270D45)),
    CardPalette(Color(0xFFE0C47B), Color(0xFFFDE9B5), Color(0xFF7A5A1C), Color(0xFF45320D)),
)

fun Long.toCardPalette(): CardPalette {
    return KahonCardPalettes.find { it.gradientStart.value.toLong() == this }
        ?: KahonCardPalettes.first()
}

val KahonRoomIcons = mapOf(
    "weekend" to Icons.Outlined.Weekend,
    "bed" to Icons.Outlined.Bed,
    "kitchen" to Icons.Outlined.Kitchen,
    "checkroom" to Icons.Outlined.Checkroom,
    "chair" to Icons.Outlined.Chair,
    "storage" to Icons.Outlined.Storage,
)

fun String.toRoomIcon(): ImageVector {
    return KahonRoomIcons[this] ?: Icons.Outlined.Weekend
}
