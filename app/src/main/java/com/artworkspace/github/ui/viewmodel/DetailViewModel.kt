package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     * Save user to database as favorite user
     *
     * @param user New favorite user
     */
    fun saveAsFavorite(user: UserEntity) {
        viewModelScope.launch {
            repository.saveUserAsFavorite(user)
        }
    }

    /**
     * Delete favorite user from database
     *
     * @param user User to delete
     */
    fun deleteFromFavorite(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteFromFavorite(user)
        }
    }

    /**
     * Determine this is favorite user or not
     *
     * @param id User id
     * @return LiveData<Boolean>
     */
    fun isFavoriteUser(id: String): LiveData<Boolean> = repository.isFavoriteUser(id)

    /**
     *  Get user detail information
     *
     *  @param username GitHub username
     *  @return LiveData<Result<User>>
     */
    fun getUserDetail(username: String) = repository.getUserDetail(username)
}