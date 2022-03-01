package com.artworkspace.github.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artworkspace.github.BuildConfig
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.remote.response.ResponseSearch
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _simpleUsers = MutableLiveData<ArrayList<SimpleUser>>()
    val simpleUsers: LiveData<ArrayList<SimpleUser>> = _simpleUsers

    init {
        findUser("\"\"")
    }

    fun getThemeSetting(): LiveData<Boolean> = repository.getThemeSetting()


    /**
     * Search GitHub user
     *
     * @param query GitHub username
     * @return Unit
     */
    fun findUser(query: String) {
        _isLoading.value = true

        ApiConfig.getApiService().searchUsername(token = "Bearer ${BuildConfig.API_KEY}", query)
            .apply {
                enqueue(object : Callback<ResponseSearch> {
                    override fun onResponse(
                        call: Call<ResponseSearch>,
                        response: Response<ResponseSearch>
                    ) {
                        if (response.isSuccessful) _simpleUsers.value = response.body()?.items
                        else Log.e(TAG, response.message())

                        _isLoading.value = false
                        _isError.value = false
                    }

                    override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                        Log.e(TAG, t.message.toString())

                        _simpleUsers.value = arrayListOf()
                        _isError.value = true
                        _isLoading.value = false
                    }

                })
            }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

}