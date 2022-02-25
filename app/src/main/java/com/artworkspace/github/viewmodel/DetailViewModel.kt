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
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _callCounter = MutableLiveData(0)
    val callCounter: LiveData<Int> = _callCounter

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    /**
     *  Get user detail information
     *
     *  @param username GitHub username
     *  @return Unit
     */
    fun getUserDetail(username: String) {
        _isLoading.value = true
        _callCounter.value = 1

        ApiConfig.getApiService().getUserDetail(token = "Bearer $TOKEN", username).apply {
            enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) _user.value = response.body()
                    else Log.e(TAG, response.message())

                    _isLoading.value = false
                    _isError.value = false
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, t.message.toString())

                    _isLoading.value = false
                    _isError.value = true
                }

            })
        }
    }

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
}