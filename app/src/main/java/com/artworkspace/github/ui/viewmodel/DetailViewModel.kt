package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.Result
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.remote.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _userDetail = MutableStateFlow<Result<User>>(Result.Loading)
    val userDetail = _userDetail.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    /**
     *  Get user detail information
     *
     *  @param username GitHub username
     *  @return Unit
     */
    fun getDetailUser(username: String) {
        _userDetail.value = Result.Loading
        viewModelScope.launch {
            repository.getUserDetail(username).collect {
                _userDetail.value = it
            }
        }

        _isLoaded.value = true
    }

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
}