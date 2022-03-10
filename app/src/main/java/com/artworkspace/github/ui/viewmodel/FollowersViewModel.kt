package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.Result
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.remote.response.SimpleUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    private val _followers = MutableStateFlow<Result<ArrayList<SimpleUser>>>(Result.Loading)
    val followers = _followers.asStateFlow()

    /**
     *  Get followers information of an user
     *
     *  @param username GitHub username
     *  @return Unit
     */
    fun getUserFollowers(username: String) {
        _followers.value = Result.Loading
        viewModelScope.launch {
            repository.getUserFollowers(username).collect {
                _followers.value = it
            }
        }

        _isLoaded.value = true
    }
}