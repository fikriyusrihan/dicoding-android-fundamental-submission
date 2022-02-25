package com.artworkspace.github.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.databinding.FragmentFollowingBinding
import com.artworkspace.github.model.SimpleUser
import com.artworkspace.github.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var _binding: FragmentFollowingBinding
    private val binding get() = _binding

    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)

        followingViewModel.following.observe(viewLifecycleOwner) { following ->
            if (following == null) {
                val username = arguments?.getString("username") ?: ""
                followingViewModel.getUserFollowing(username)
            } else {
                showFollowing(following)
            }
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    /**
     * Showing up result, setup layout manager, adapter, and onClickItemCallback
     *
     * @param users Following
     * @return Unit
     */
    private fun showFollowing(users: ArrayList<SimpleUser>) {
        val linearLayoutManager = LinearLayoutManager(activity)
        val listAdapter = ListUserAdapter(users)

        binding.rvUsers.apply {
            layoutManager = linearLayoutManager
            adapter = listAdapter
            setHasFixedSize(true)
        }

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: SimpleUser) {
                goToDetailUser(user)
            }

        })
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