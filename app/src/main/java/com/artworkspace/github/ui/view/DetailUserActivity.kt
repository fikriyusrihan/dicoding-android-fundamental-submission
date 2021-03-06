package com.artworkspace.github.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.artworkspace.github.R
import com.artworkspace.github.adapter.SectionPagerAdapter
import com.artworkspace.github.data.Result
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.remote.response.User
import com.artworkspace.github.databinding.ActivityDetailUserBinding
import com.artworkspace.github.ui.viewmodel.DetailViewModel
import com.artworkspace.github.utils.EspressoIdlingResource
import com.artworkspace.github.utils.UIHelper.Companion.setAndVisible
import com.artworkspace.github.utils.UIHelper.Companion.setImageGlide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!

    private var username: String? = null
    private var profileUrl: String? = null
    private var userDetail: UserEntity? = null
    private var isFavorite: Boolean? = false

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        username = intent.extras?.get(EXTRA_DETAIL) as String

        setContentView(binding.root)
        setViewPager()
        setToolbar(getString(R.string.profile))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    detailViewModel.userDetail.collect { result ->
                        onDetailUserReceived(result)
                    }
                }
                launch {
                    detailViewModel.isFavoriteUser(username ?: "").collect { state ->
                        isFavoriteUser(state)
                        isFavorite = state
                    }
                }
                launch {
                    detailViewModel.isLoaded.collect { loaded ->
                        if (!loaded) detailViewModel.getDetailUser(username ?: "")
                    }
                }
            }
        }

        binding.btnOpen.setOnClickListener(this)
        binding.fabFavorite.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        EspressoIdlingResource.increment()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_open -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(profileUrl)
                }.also {
                    startActivity(it)
                }
            }
            R.id.fab_favorite -> {
                if (isFavorite == true) {
                    userDetail?.let { detailViewModel.deleteFromFavorite(it) }
                    isFavoriteUser(false)
                    Toast.makeText(this, "User deleted from favorite", Toast.LENGTH_SHORT).show()
                } else {
                    userDetail?.let { detailViewModel.saveAsFavorite(it) }
                    isFavoriteUser(true)
                    Toast.makeText(this, "User added to favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        username = null
        profileUrl = null
        isFavorite = null
        super.onDestroy()
    }

    /**
     * Parsing data to UI based on result
     *
     * @param result Result from API
     */
    private fun onDetailUserReceived(result: Result<User>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                errorOccurred()
                showLoading(false)
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
            is Result.Success -> {
                result.data.let { user ->
                    parseUserDetail(user)

                    val userEntity = UserEntity(
                        user.login,
                        user.avatarUrl,
                        true
                    )

                    userDetail = userEntity
                    profileUrl = user.htmlUrl
                }

                showLoading(false)
                EspressoIdlingResource.decrement()
            }
        }
    }

    /**
     * Determine which icon to display in FAB
     *
     * @param favorite  Is this favorite user?
     */
    private fun isFavoriteUser(favorite: Boolean) {
        if (favorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    /**
     * Setting UI when an error occurred
     *
     * @return Unit
     */
    private fun errorOccurred() {
        binding.apply {
            userDetailContainer.visibility = View.INVISIBLE
            tabs.visibility = View.INVISIBLE
            viewPager.visibility = View.INVISIBLE
        }
    }

    /**
     * Setting up toolbar
     *
     * @param title Toolbar title
     * @return Unit
     */
    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbarDetail)
        binding.collapsingToolbar.isTitleEnabled = false
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    /**
     * Setting up viewpager
     *
     * @return Unit
     */
    private fun setViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        viewPager.adapter = SectionPagerAdapter(this, username!!)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    /**
     * Showing loading indicator
     *
     * @param isLoading Loading state
     * @return Unit
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                pbLoading.visibility = View.VISIBLE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
                fabFavorite.visibility = View.GONE
            }
        } else {
            binding.apply {
                pbLoading.visibility = View.GONE
                appBarLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                fabFavorite.visibility = View.VISIBLE
            }
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
            tvUsername.text = user.login
            tvRepositories.text = user.publicRepos.toString()
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()

            tvName.setAndVisible(user.name)
            tvBio.setAndVisible(user.bio)
            tvCompany.setAndVisible(user.company)
            tvLocation.setAndVisible(user.location)
            tvBlog.setAndVisible(user.blog)

            ivAvatar.setImageGlide(this@DetailUserActivity, user.avatarUrl)
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}