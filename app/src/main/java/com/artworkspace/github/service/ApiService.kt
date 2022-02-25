package com.artworkspace.github.service

import com.artworkspace.github.model.ResponseFollowers
import com.artworkspace.github.model.ResponseSearch
import com.artworkspace.github.model.SimpleUser
import com.artworkspace.github.model.User
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
    fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<User>


    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<SimpleUser>>
}