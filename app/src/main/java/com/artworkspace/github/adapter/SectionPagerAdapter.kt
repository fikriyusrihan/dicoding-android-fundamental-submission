package com.artworkspace.github.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artworkspace.github.ui.view.FollowersFragment
import com.artworkspace.github.ui.view.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FollowersFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARGS_USERNAME, username)
                    }
                }
            }
            else -> {
                FollowingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARGS_USERNAME, username)
                    }
                }
            }
        }
    }

    companion object {
        const val ARGS_USERNAME = "username"
    }
}