package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.local.entity.UserEntity

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     * Get all favorite users from database
     *
     * @return LiveData<List<UserEntity>>
     */
    fun getFavoriteUsers(): LiveData<List<UserEntity>> = repository.getAllFavoriteUsers()
}