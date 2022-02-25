package com.artworkspace.github.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artworkspace.github.Utils
import com.artworkspace.github.model.SimpleUser
import com.artworkspace.github.repository.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _following = MutableLiveData<ArrayList<SimpleUser>>()
    val following: LiveData<ArrayList<SimpleUser>> = _following

    fun getUserFollowing(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserFollowing(token = "Bearer ${Utils.TOKEN}", username)
            .apply {
                enqueue(object : Callback<ArrayList<SimpleUser>> {
                    override fun onResponse(
                        call: Call<ArrayList<SimpleUser>>,
                        response: Response<ArrayList<SimpleUser>>
                    ) {
                        _isLoading.value = false
                        if (response.isSuccessful) _following.value = response.body()
                        else Log.e(TAG, response.message())
                    }

                    override fun onFailure(call: Call<ArrayList<SimpleUser>>, t: Throwable) {
                        Log.e(TAG, t.message.toString())
                    }

                })
            }
    }

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }
}