package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    /**
     * Get theme setting from DataStore
     *
     * @return LiveData<Boolean>
     */
    val getThemeSetting: Flow<Boolean> = userRepository.getThemeSetting()

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