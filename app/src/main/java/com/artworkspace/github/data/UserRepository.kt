package com.artworkspace.github.data

import android.util.Log
import com.artworkspace.github.BuildConfig
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.local.room.UserDao
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.data.remote.response.User
import com.artworkspace.github.data.remote.retrofit.ApiService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val preferences: AppPreferences
) {

    /**
     * Search GitHub user with API
     *
     * @param q GitHub username query
     * @return LiveData<Result<ArrayList<SimpleUser>>>
     */
    fun searchUserByUsername(q: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)
        try {
            val users = apiService.searchUsername(token = API_TOKEN, q).items
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "searchUserByUsername: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    /**
     *  Get following information of an user from API
     *
     *  @param id GitHub username
     *  @return Flow<Result<ArrayList<SimpleUser>>>
     */
    fun getUserFollowing(id: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)
        try {
            val users = apiService.getUserFollowing(token = API_TOKEN, id)
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowing: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    /**
     *  Get followers information of an user from API
     *
     *  @param id GitHub username
     *  @return Flow<Result<ArrayList<SimpleUser>>>
     */
    fun getUserFollowers(id: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)
        try {
            val users = apiService.getUserFollowers(token = API_TOKEN, id)
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowers: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    /**
     *  Get user detail information from API
     *
     *  @param id GitHub username
     *  @return LiveData<Result<User>>
     */
    fun getUserDetail(id: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try {
            val user = apiService.getUserDetail(token = API_TOKEN, id)
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "getUserDetail: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    /**
     * Determine this user is favorite or not
     *
     * @param id User id
     * @return Flow<Boolean>
     */
    fun isFavoriteUser(id: String): Flow<Boolean> = userDao.isFavoriteUser(id)

    /**
     * Get all favorite users from database
     *
     * @return LiveData<List<UserEntity>>
     */
    fun getAllFavoriteUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    /**
     * Delete a favorite user from database
     *
     * @param user User to delete
     */
    suspend fun deleteFromFavorite(user: UserEntity) {
        userDao.delete(user)
    }

    /**
     * Save user as favorite to database
     *
     * @param user User to save
     */
    suspend fun saveUserAsFavorite(user: UserEntity) {
        userDao.insert(user)
    }

    /**
     * Get theme setting for dark mode state from DataStore
     *
     * @return Flow<Boolean>
     */
    fun getThemeSetting(): Flow<Boolean> = preferences.getThemeSetting()

    /**
     * Save theme setting for dark mode state to DataStore
     *
     * @param isDarkModeActive Dark mode state to save
     */
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        preferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        private const val API_TOKEN = "Bearer ${BuildConfig.API_KEY}"
        private val TAG = UserRepository::class.java.simpleName
    }
}