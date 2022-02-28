package com.artworkspace.github.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.artworkspace.github.R
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.databinding.ActivityMainBinding
import com.artworkspace.github.model.SimpleUser
import com.artworkspace.github.ui.DetailUserActivity.Companion.EXTRA_DETAIL
import com.artworkspace.github.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mainViewModel.simpleUsers.observe(this) {
            showSearchingResult(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isError.observe(this) { error ->
            if (error) errorOccurred()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.github_username)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.findUser(query ?: "")
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show()
            }
            R.id.setting -> {
                Intent(this@MainActivity, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * Setting UI when an error occurred
     *
     * @return Unit
     */
    private fun errorOccurred() {
        Toast.makeText(this@MainActivity, "An Error is Occurred", Toast.LENGTH_SHORT).show()
    }

    /**
     * Determine loading indicator is visible or not
     *
     * @param isLoading Loading state
     * @return Unit
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }

    /**
     * Showing up result, setup layout manager, adapter, and onClickItemCallback
     *
     * @param user List of Users
     * @return Unit
     */
    private fun showSearchingResult(user: ArrayList<SimpleUser>) {
        binding.tvResultCount.text = getString(R.string.showing_results, user.size)

        val listUserAdapter = ListUserAdapter(user)

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listUserAdapter
            setHasFixedSize(true)
        }

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: SimpleUser) {
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
    private fun goToDetailUser(user: SimpleUser) {
        Intent(this@MainActivity, DetailUserActivity::class.java).apply {
            putExtra(EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }
}