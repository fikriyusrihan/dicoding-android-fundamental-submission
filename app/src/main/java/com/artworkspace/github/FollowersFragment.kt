package com.artworkspace.github

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.databinding.FragmentFollowersBinding
import com.artworkspace.github.model.SimpleUser
import com.artworkspace.github.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {

    private lateinit var _binding: FragmentFollowersBinding
    private val binding get() = _binding

    private val followersViewModel by viewModels<FollowersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(layoutInflater, container, false)

        val layoutManager = LinearLayoutManager(activity)
        binding.rvUsers.layoutManager = layoutManager

        val username = arguments?.getString("username") ?: ""
        followersViewModel.getUserFollowers(username)

        followersViewModel.followers.observe(viewLifecycleOwner) {
            showFollowers(it)
        }

        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    private fun showFollowers(users: ArrayList<SimpleUser>) {
        val adapter = ListUserAdapter(users)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }

}