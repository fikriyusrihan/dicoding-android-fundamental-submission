package com.artworkspace.github.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artworkspace.github.Utils.Companion.TOKEN
import com.artworkspace.github.model.ResponseSearch
import com.artworkspace.github.model.SimpleUser
import com.artworkspace.github.repository.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _simpleUsers = MutableLiveData<ArrayList<SimpleUser>>()
    val simpleUsers: LiveData<ArrayList<SimpleUser>> = _simpleUsers

    init {
        findUser("\"\"")
    }

    /**
     * Search GitHub user
     *
     * @param query GitHub username
     * @return Unit
     */
    fun findUser(query: String) {
        _isLoading.value = true

        ApiConfig.getApiService().searchUsername(token = "Bearer $TOKEN", query).apply {
            enqueue(object : Callback<ResponseSearch> {
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    if (response.isSuccessful) _simpleUsers.value = response.body()?.items
                    else Log.e(TAG, response.message())
                    _isLoading.value = false
                }

                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

}