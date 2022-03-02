package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.artworkspace.github.data.UserRepository

class FollowingViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     *  Get following information of an user
     *
     *  @param username GitHub username
     *  @return Unit
     */
    fun getUserFollowing(username: String) = repository.getUserFollowing(username)
}