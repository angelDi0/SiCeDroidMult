package com.example.sicedroidmult.viewmodel

sealed interface SNUiState {
    object Idle : SNUiState
    object Loading : SNUiState
    object Success : SNUiState
    data class Error(val message: String) : SNUiState
    data class Syncing(val message: String) : SNUiState
}