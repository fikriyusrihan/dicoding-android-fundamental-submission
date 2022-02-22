package com.artworkspace.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.artworkspace.github.databinding.UserCardBinding
import com.artworkspace.github.model.User

class ListUserAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.cardTvName.text = user.name
        holder.binding.cardTvCompany.text = user.company
        holder.binding.cardImageProfile.setImageResource(user.avatar)

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, user.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}