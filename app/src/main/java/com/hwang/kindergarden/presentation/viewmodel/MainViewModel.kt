package com.hwang.kindergarden.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwang.kindergarden.data.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    
    private val _isLoading = kotlinx.coroutines.flow.MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    
    val shouldShowWelcome: StateFlow<Boolean> = dataStoreManager.shouldShowWelcome
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
    
    init {
        viewModelScope.launch {
            dataStoreManager.shouldShowWelcome.collect {
                _isLoading.value = false
            }
        }
    }
    
    fun setShouldShowWelcome(show: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            dataStoreManager.setShouldShowWelcome(show)
            _isLoading.value = false
        }
    }
} 