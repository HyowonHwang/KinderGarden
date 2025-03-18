package com.hwang.kindergarden.presentation.viewmodel

import VideoContent
import VideoContentList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwang.kindergarden.domain.repository.VideoContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoContentViewModel @Inject constructor(
    private val repository: VideoContentRepository
) : ViewModel() {

    // UI 상태를 나타내는 sealed interface
    sealed interface UiState {
        object Loading : UiState
        data class Success(val videoContents: VideoContentList) : UiState
        data class Error(val message: String) : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedVideo = MutableStateFlow<VideoContent?>(null)
    val selectedVideo: StateFlow<VideoContent?> = _selectedVideo.asStateFlow()

    init {
        loadVideoContents()
    }

    fun loadVideoContents() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getVideoContents()
                .onSuccess { contents ->
                    _uiState.value = UiState.Success(contents)
                }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Unknown error occurred")
                }
        }
    }

    fun getVideoContent(id: Int) {
        viewModelScope.launch {
            repository.getVideoContentById(id)
                .onSuccess { video ->
                    _selectedVideo.value = video
                }
                .onFailure { exception ->
                    // 에러 처리
                    _uiState.update { UiState.Error(exception.message ?: "Failed to load video") }
                }
        }
    }

    fun clearSelectedVideo() {
        _selectedVideo.value = null
    }

    fun retryLoading() {
        loadVideoContents()
    }
} 