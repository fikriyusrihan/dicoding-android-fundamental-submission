package com.artworkspace.github.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artworkspace.github.Utils.Companion.TOKEN
import com.artworkspace.github.model.User
import com.artworkspace.github.repository.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    /**
     *  Get user detail information
     *
     *  @param username GitHub username
     *  @return Unit
     */
    fun getUserDetail(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserDetail(token = "Bearer $TOKEN", username).apply {
            enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    _isLoading.value = false
                    if (response.isSuccessful) _user.value = response.body()
                    else Log.e(TAG, response.message())
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
        }
    }

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
}