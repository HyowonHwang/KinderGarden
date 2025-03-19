package com.hwang.kindergarden.presentation.viewmodel

import MealData
import MealType
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwang.kindergarden.domain.repository.UploadPhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MealDialogViewModel @Inject constructor(
    private val repository: UploadPhotoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealUiState())
    val uiState: StateFlow<MealUiState> = _uiState.asStateFlow()

    fun updateMealType(type: MealType) {
        _uiState.value = _uiState.value.copy(selectedMealType = type)
    }

    fun updateMealContent(content: String) {
        _uiState.value = _uiState.value.copy(mealContent = content)
    }

    fun uploadPhoto(context: Context, file: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true)
            repository.uploadPhoto(context, file)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        uploadError = e.message,
                        isUploading = false
                    )
                }
                .collect { result ->
                    result.onSuccess { uri ->
                        _uiState.value = _uiState.value.copy(
                            thumbnailUri = uri,
                            uploadError = null,
                            isUploading = false
                        )
                    }.onFailure { error ->
                        _uiState.value = _uiState.value.copy(
                            uploadError = error.message,
                            isUploading = false
                        )
                    }
                }
        }
    }

    fun getMealData(): MealData {
        return MealData(
            type = _uiState.value.selectedMealType,
            content = _uiState.value.mealContent,
            thumbnailUrl = uiState.value.thumbnailUri ?: ""
        )
    }
}

data class MealUiState(
    val selectedMealType: MealType = MealType.BREAKFAST,
    val mealContent: String = "",
    val thumbnailUri: String? = null,
    val isUploading: Boolean = false,
    val uploadError: String? = null
) 