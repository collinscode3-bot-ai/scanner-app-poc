package com.example.numberscanner.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.numberscanner.data.model.ScanRecord
import com.example.numberscanner.data.repository.ScanRepository
import kotlinx.coroutines.launch

data class ScannerUiState(
    val scannedNumber: String = "",
    val classification: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

class ScannerViewModel(private val repository: ScanRepository) : ViewModel() {
    private val _uiState = mutableStateOf(ScannerUiState())
    val uiState: State<ScannerUiState> = _uiState

    fun onNumberScanned(number: String) {
        if (_uiState.value.scannedNumber != number) {
            _uiState.value = _uiState.value.copy(scannedNumber = number)
        }
    }

    fun onClassificationChanged(classification: String) {
        _uiState.value = _uiState.value.copy(classification = classification)
    }

    fun onNotesChanged(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun saveRecord() {
        val currentState = _uiState.value
        if (currentState.scannedNumber.isBlank()) return

        _uiState.value = currentState.copy(isSaving = true)
        viewModelScope.launch {
            val record = ScanRecord(
                serialNumber = currentState.scannedNumber,
                classification = currentState.classification,
                notes = currentState.notes
            )
            repository.insert(record)
            _uiState.value = currentState.copy(
                isSaving = false,
                saveSuccess = true,
                scannedNumber = "",
                classification = "",
                notes = ""
            )
        }
    }

    fun resetScanner() {
        _uiState.value = ScannerUiState()
    }
}

class ScannerViewModelFactory(private val repository: ScanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScannerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
