package com.artworkspace.github.ui.view

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
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.artworkspace.github.R
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.data.Result
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.databinding.ActivityMainBinding
import com.artworkspace.github.ui.view.DetailUserActivity.Companion.EXTRA_DETAIL
import com.artworkspace.github.ui.viewmodel.MainViewModel
import com.artworkspace.github.utils.EspressoIdlingResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.themeSetting.collect { state ->
                        if (state) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
                launch {
                    mainViewModel.users.collect { result ->
                        showSearchingResult(result)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EspressoIdlingResource.increment()
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
                    EspressoIdlingResource.increment()
                    mainViewModel.searchUserByUsername(query ?: "")
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
                Intent(this@MainActivity, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
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
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
            binding.rvUsers.visibility = View.GONE
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.rvUsers.visibility = View.VISIBLE
        }
    }

    /**
     * Showing up result, setup layout manager, adapter, and onClickItemCallback
     *
     * @param result Result from viewmodel
     * @return Unit
     */
    private fun showSearchingResult(result: Result<ArrayList<SimpleUser>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                errorOccurred()
                showLoading(false)
            }
            is Result.Success -> {
                binding.tvResultCount.text = getString(R.string.showing_results, result.data.size)
                val listUserAdapter = ListUserAdapter(result.data)

                binding.rvUsers.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = listUserAdapter
                    setHasFixedSize(true)
                }

                listUserAdapter.setOnItemClickCallback(object :
                    ListUserAdapter.OnItemClickCallback {
                    override fun onItemClicked(user: SimpleUser) {
                        goToDetailUser(user)
                    }

                })
                showLoading(false)
                EspressoIdlingResource.decrement()
            }
        }
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