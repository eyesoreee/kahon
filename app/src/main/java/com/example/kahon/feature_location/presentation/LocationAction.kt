package com.example.kahon.feature_location.presentation

sealed interface LocationAction {
    data class OnLocationClick(val id: Long, val name: String) : LocationAction
    data object OnAddLocationClick : LocationAction
}