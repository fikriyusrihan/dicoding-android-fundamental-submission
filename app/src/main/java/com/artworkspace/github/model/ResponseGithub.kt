package com.artworkspace.github.model

import com.google.gson.annotations.SerializedName

data class ResponseSearch(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<SimpleUser>
)

data class ResponseFollowers(
	val responseUser: List<SimpleUser>
)

data class ResponseFollowing(
	val responseUser: List<SimpleUser>
)

data class User2(

	@field:SerializedName("twitter_username")
	val twitterUsername: String,

	@field:SerializedName("bio")
	val bio: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("blog")
	val blog: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("company")
	val company: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("public_repos")
	val publicRepos: Int,

	@field:SerializedName("email")
	val email: Any
)

data class SimpleUser(

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("url")
	val url: String
)


