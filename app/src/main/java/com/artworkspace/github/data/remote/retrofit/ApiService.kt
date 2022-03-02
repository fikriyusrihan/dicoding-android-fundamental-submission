package com.artworkspace.github.data.remote.retrofit

import com.artworkspace.github.data.remote.response.ResponseSearch
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.data.remote.response.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /**
     * Search GitHub user with username
     *
     * @param token GitHub token auth
     * @param q Query
     * @return Call<ResponseSearch>
     */
    @GET("search/users")
    fun searchUsername(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ): Call<ResponseSearch>


    /**
     * Get detail information of user by username
     *
     * @param token GitHub token auth
     * @param username Username
     * @return Call<User>
     */
    @GET("users/{username}")
    suspend fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): User


    /**
     * Get followers information of an user
     *
     * @param token GitHub token auth
     * @param username Username
     * @return Call<ArrayList<SimpleUser>>
     */
    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): ArrayList<SimpleUser>


    /**
     * Get following information of an user
     *
     * @param token GitHub token auth
     * @param username Username
     * @return Call<ArrayList<SimpleUser>>
     */
    @GET("users/{username}/following")
    fun getUserFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<SimpleUser>>
}