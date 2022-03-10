package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _favorites = MutableStateFlow(listOf<UserEntity>())
    val favorite = _favorites.asStateFlow()

    init {
        getFavoriteUsers()
    }

    /**
     * Get all favorite users from database
     *
     * @return LiveData<List<UserEntity>>
     */
    private fun getFavoriteUsers() {
        viewModelScope.launch {
            repository.getAllFavoriteUsers().collect {
                _favorites.value = it
            }
        }
    }
}