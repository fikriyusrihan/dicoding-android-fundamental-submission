package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getLastSearchQuery(): LiveData<String> = repository.getLastSearchQuery()

    fun saveLastSearchQuery(query: String) {
        viewModelScope.launch {
            repository.saveLastSearchQuery(query)
        }
    }

    /**
     * Get theme setting for dark mode from DataStore
     *
     * @return LiveData<Boolean>
     */
    fun getThemeSetting(): LiveData<Boolean> = repository.getThemeSetting()


    /**
     * Search GitHub user
     *
     * @param query GitHub username
     * @return Unit
     */
    fun searchUserByUsername(query: String) = repository.searchUserByUsername(query)
}