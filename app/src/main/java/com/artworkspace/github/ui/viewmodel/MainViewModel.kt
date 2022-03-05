package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.Result
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.remote.response.SimpleUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    val themeSetting: Flow<Boolean> = repository.getThemeSetting()

    private val _users = MutableStateFlow<Result<ArrayList<SimpleUser>>>(Result.Loading)
    val users = _users.asStateFlow()

    init {
        searchUserByUsername("\"\"")
    }

    /**
     * Search GitHub user
     *
     * @param query GitHub username
     * @return LiveData<Result<ArrayList<SimpleUser>
     */
    fun searchUserByUsername(query: String) {
        _users.value = Result.Loading
        viewModelScope.launch {
            repository.searchUserByUsername(query).collect {
                _users.value = it
            }
        }
    }
}