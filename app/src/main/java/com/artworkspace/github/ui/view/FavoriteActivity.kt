package com.artworkspace.github.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.artworkspace.github.R
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.databinding.ActivityFavoriteBinding
import com.artworkspace.github.ui.viewmodel.FavoriteViewModel
import com.artworkspace.github.utils.EspressoIdlingResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar(getString(R.string.favorite))

        lifecycleScope.launchWhenStarted {
            launch {
                favoriteViewModel.favorite.collect {
                    EspressoIdlingResource.increment()
                    if (it.isNotEmpty()) showFavoriteUsers(it)
                    else showMessage()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showMessage() {
        binding.tvMessage.visibility = View.VISIBLE
        binding.rvFavorite.visibility = View.GONE

        EspressoIdlingResource.decrement()
    }

    /**
     * Convert data type and display favorite users to the recycler view
     *
     * @param users List of favorite users
     * @return Unit
     */
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
            visibility = View.VISIBLE
            setHasFixedSize(true)
        }

        binding.tvMessage.visibility = View.GONE

        listUserAdapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: SimpleUser) {
                goToDetailUser(user)
            }
        })

        EspressoIdlingResource.decrement()
    }

    /**
     * Go to detail page with selected user data
     *
     * @param user  Selected user
     * @return Unit
     */
    private fun goToDetailUser(user: SimpleUser) {
        Intent(this@FavoriteActivity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }

    /**
     * Setting up toolbar
     *
     * @param title Toolbar title
     * @return Unit
     */
    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }
}