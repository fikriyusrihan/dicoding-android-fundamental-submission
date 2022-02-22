package com.artworkspace.github.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artworkspace.github.R
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.databinding.ActivityMainBinding
import com.artworkspace.github.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView

    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUsers = binding.rvUsers
        rvUsers.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecyclerView()
    }

    private val listUsers: ArrayList<User>
        get() {
            val dataUsername = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getIntArray(R.array.repository)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataFollowers = resources.getIntArray(R.array.followers)
            val dataFollowing = resources.getIntArray(R.array.following)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)

            val listUsers = ArrayList<User>()

            for (i in dataUsername.indices) {
                val user = User(
                    dataUsername[i],
                    dataName[i],
                    dataLocation[i],
                    dataRepository[i],
                    dataCompany[i],
                    dataFollowers[i],
                    dataFollowing[i],
                    dataAvatar.getResourceId(i, -1)
                )
                listUsers.add(user)
            }

            return listUsers
        }

    private fun showRecyclerView() {
        rvUsers.layoutManager = LinearLayoutManager(this)

        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter
    }
}