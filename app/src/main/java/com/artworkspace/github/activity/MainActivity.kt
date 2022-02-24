package com.artworkspace.github.activity

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artworkspace.github.R
import com.artworkspace.github.activity.DetailUserActivity.Companion.EXTRA_DETAIL
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.databinding.ActivityMainBinding
import com.artworkspace.github.model.User
import com.artworkspace.github.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView

    private val list = ArrayList<User>()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        rvUsers = binding.rvUsers
        rvUsers.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.github_username)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(this@MainActivity, "$query", Toast.LENGTH_SHORT).show()
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }

        return true
    }

    private val listUsers: ArrayList<User>
        @SuppressLint("Recycle")
        get() {
            val dataUsername = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataFollowers = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)

            val listUsers = ArrayList<User>()

            for (i in dataUsername.indices) {
                val user = User(
                    dataUsername[i],
                    dataName[i],
                    dataLocation[i],
                    dataRepository[i].toInt(),
                    dataCompany[i],
                    dataFollowers[i].toInt(),
                    dataFollowing[i].toInt(),
                    dataAvatar.getResourceId(i, -1)
                )
                listUsers.add(user)
            }

            return listUsers
        }


    /**
     * Setting up layout manager, adapter, and onClickItemCallback
     */
    private fun showRecyclerView() {
        rvUsers.layoutManager = LinearLayoutManager(this)

        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                goToDetailUser(user)
            }

        })
    }


    /**
     * Go to detail page with selected user data
     *
     * @param user  Selected user
     * @return Unit
     */
    private fun goToDetailUser(user: User) {
        Intent(this, DetailUserActivity::class.java).apply {
            putExtra(EXTRA_DETAIL, user)
        }.also {
            startActivity(it)
        }
    }
}