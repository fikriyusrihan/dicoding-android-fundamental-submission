package com.artworkspace.github.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artworkspace.github.ui.FollowersFragment
import com.artworkspace.github.ui.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FollowersFragment().apply {
                    arguments = Bundle().apply {
                        putString("username", username)
                    }
                }
            }
            else -> {
                FollowingFragment().apply {
                    arguments = Bundle().apply {
                        putString("username", username)
                    }
                }
            }
        }
    }
}