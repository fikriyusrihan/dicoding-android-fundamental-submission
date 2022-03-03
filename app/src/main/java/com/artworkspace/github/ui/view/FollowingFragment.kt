package com.artworkspace.github.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.adapter.SectionPagerAdapter.Companion.ARGS_USERNAME
import com.artworkspace.github.data.Result
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.databinding.FragmentFollowingBinding
import com.artworkspace.github.ui.viewmodel.FollowingViewModel
import com.artworkspace.github.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val followingViewModel by viewModels<FollowingViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARGS_USERNAME) ?: ""
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                followingViewModel.following.collect {
                    onFollowingResultReceived(it)
                }
            }
            launch {
                followingViewModel.isLoaded.collect { loaded ->
                    if (!loaded) followingViewModel.getUserFollowing(username)
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * Parsing data to UI based on result
     *
     * @param result Result from API
     */
    private fun onFollowingResultReceived(result: Result<ArrayList<SimpleUser>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                showLoading(false)
            }
            is Result.Success -> {
                showFollowing(result.data)
                showLoading(false)
            }
        }
    }

    /**
     * Showing up result, setup layout manager, adapter, and onClickItemCallback
     *
     * @param users Following
     * @return Unit
     */
    private fun showFollowing(users: ArrayList<SimpleUser>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = ListUserAdapter(users)

            binding.rvUsers.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object :
                ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: SimpleUser) {
                    goToDetailUser(user)
                }

            })
        } else binding.tvStatus.visibility = View.VISIBLE
    }

    /**
     * Showing loading indicator
     *
     * @param isLoading Loading state
     * @return Unit
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }

    /**
     * Go to detail page with selected user data
     *
     * @param user  Selected user
     * @return Unit
     */
    private fun goToDetailUser(user: SimpleUser) {
        Intent(activity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }
}