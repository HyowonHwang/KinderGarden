package com.hwang.kindergarden.ui.screens

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
    
    val shouldShowWelcome: StateFlow<Boolean> = dataStoreManager.shouldShowWelcome
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
    
    fun setShouldShowWelcome(show: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setShouldShowWelcome(show)
        }
    }
} 