package com.artworkspace.github.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.artworkspace.github.R
import com.artworkspace.github.Utils.Companion.setAndVisible
import com.artworkspace.github.Utils.Companion.setImageGlide
import com.artworkspace.github.databinding.ActivityDetailUserBinding
import com.artworkspace.github.model.User
import com.artworkspace.github.viewmodel.DetailViewModel

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var username: String
    private lateinit var user: User

    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.profile)
        }

        username = intent.extras?.get(EXTRA_DETAIL) as String
        detailViewModel.getUserDetail(username)

        detailViewModel.user.observe(this) {
            user = it
            parseUserDetail(user)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnOpen.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_open -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(user.htmlUrl)
                }.also {
                    startActivity(it)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
            binding.scContentContainer.visibility = View.GONE
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.scContentContainer.visibility = View.VISIBLE
        }
    }

    /**
     * Parsing User data to it's view
     *
     * @param user  User dataclass
     * @return Unit
     */
    private fun parseUserDetail(user: User) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvRepositories.text = user.publicRepos.toString()
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()

            tvBio.setAndVisible(user.bio)
            tvCompany.setAndVisible(user.company)
            tvLocation.setAndVisible(user.location)
            tvBlog.setAndVisible(user.blog)

            ivAvatar.setImageGlide(this@DetailUserActivity, user.avatarUrl)
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}