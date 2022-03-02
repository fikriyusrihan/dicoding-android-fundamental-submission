package com.artworkspace.github.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.remote.response.User
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _callCounter = MutableLiveData(0)
    val callCounter: LiveData<Int> = _callCounter

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

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
     *  @return Unit
     */
    fun getUserDetail(username: String) = repository.getUserDetail(username)

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
}