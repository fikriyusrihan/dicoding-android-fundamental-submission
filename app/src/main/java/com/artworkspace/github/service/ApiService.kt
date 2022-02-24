package com.artworkspace.github.service

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUsername(
        @Query("q") q: String
    )
}