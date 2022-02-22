package com.artworkspace.github.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.artworkspace.github.R
import com.artworkspace.github.databinding.ActivityDetailUserBinding
import com.artworkspace.github.model.User

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.extras?.get(EXTRA_DETAIL) as User
        parseUserDetail(user)

        binding.btnBack.setOnClickListener(this)
        binding.btnOpen.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                finish()
            }

            R.id.btn_open -> {
                val url = "https://www.github.com/${user.username}"
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }.also {
                    startActivity(it)
                }
            }
        }
    }

    private fun parseUserDetail(user: User) {
        binding.tvName.text = user.name
        binding.tvUsername.text = user.username
        binding.tvRepositories.text = user.repository.toString()
        binding.tvFollowers.text = user.follower.toString()
        binding.tvFollowing.text = user.following.toString()
        binding.tvCompany.text = user.company
        binding.tvLocation.text = user.location
        binding.ivAvatar.setImageResource(user.avatar)
    }
}