package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.artworkspace.github.data.UserRepository

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    fun getFavoriteUsers() = repository.getAllFavoriteUsers()
}