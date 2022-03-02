package com.artworkspace.github.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.artworkspace.github.R
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.databinding.ActivityFavoriteBinding
import com.artworkspace.github.ui.viewmodel.FavoriteViewModel
import com.artworkspace.github.ui.viewmodel.ViewModelFactory


class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()

        favoriteViewModel.getFavoriteUsers().observe(this) { users ->
            showFavoriteUsers(users)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showFavoriteUsers(users: List<UserEntity>) {
        val listUsers = ArrayList<SimpleUser>()

        users.forEach { user ->
            val data = SimpleUser(
                user.avatarUrl,
                user.id
            )

            listUsers.add(data)
        }

        val listUserAdapter = ListUserAdapter(listUsers)

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = listUserAdapter
            setHasFixedSize(true)
        }

        listUserAdapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: SimpleUser) {
                goToDetailUser(user)
            }

        })
    }

    private fun goToDetailUser(user: SimpleUser) {
        Intent(this@FavoriteActivity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.favorite)
        }
    }
}