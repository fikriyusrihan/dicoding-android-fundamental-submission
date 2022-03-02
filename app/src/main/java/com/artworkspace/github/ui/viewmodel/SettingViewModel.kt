package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {

    /**
     * Get theme setting from DataStore
     *
     * @return LiveData<Boolean>
     */
    fun getThemeSetting(): Flow<Boolean> = userRepository.getThemeSetting()

    val themeSetting: StateFlow<Boolean> = userRepository.getThemeSetting().stateIn(
        initialValue = false,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    /**
     * Saving dark mode state to DataStore
     *
     * @param darkModeState Dark mode state
     */
    fun saveThemeSetting(darkModeState: Boolean) {
        viewModelScope.launch {
            userRepository.saveThemeSetting(darkModeState)
        }
    }
}