package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.artworkspace.github.data.UserRepository

class FollowersViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     *  Get followers information of an user
     *
     *  @param username GitHub username
     *  @return Unit
     */
    fun getUserFollowers(username: String) = repository.getUserFollowers(username)
}