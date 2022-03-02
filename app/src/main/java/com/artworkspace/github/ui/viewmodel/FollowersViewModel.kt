package com.artworkspace.github.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artworkspace.github.BuildConfig
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     *  Get followers information of an user
     *
     *  @param username GitHub username
     *  @return Unit
     */
    fun getUserFollowers(username: String) = repository.getUserFollowers(username)

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
    }

}