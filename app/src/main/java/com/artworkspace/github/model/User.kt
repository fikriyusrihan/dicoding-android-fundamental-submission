package com.artworkspace.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
    val name: String,
    val location: String,
    val repository: Int,
    val company: String,
    val follower: Int,
    val following: Int,
    val avatar: Int
) : Parcelable