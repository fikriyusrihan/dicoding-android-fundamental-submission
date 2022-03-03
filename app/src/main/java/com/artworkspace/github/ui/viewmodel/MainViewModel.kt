package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    val themeSetting: Flow<Boolean> = repository.getThemeSetting()

    /**
     * Get last search query in search view
     *
     * @return LiveData<String>
     */
    fun getLastSearchQuery(): LiveData<String> = repository.getLastSearchQuery()

    /**
     * Saving last search query in search view
     *
     * @param query Search query
     */
    fun saveLastSearchQuery(query: String) {
        viewModelScope.launch {
            repository.saveLastSearchQuery(query)
        }
    }

    /**
     * Search GitHub user
     *
     * @param query GitHub username
     * @return LiveData<Result<ArrayList<SimpleUser>
     */
    fun searchUserByUsername(query: String) = repository.searchUserByUsername(query)
}