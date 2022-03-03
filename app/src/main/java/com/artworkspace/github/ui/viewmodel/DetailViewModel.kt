package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.Result
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.remote.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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
    fun isFavoriteUser(id: String): Flow<Boolean> = repository.isFavoriteUser(id)

    /**
     *  Get user detail information
     *
     *  @param username GitHub username
     *  @return LiveData<Result<User>>
     */
    fun getUserDetail(username: String): StateFlow<Result<User>> =
        repository.getUserDetail(username).stateIn(
            initialValue = Result.Loading,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )


}