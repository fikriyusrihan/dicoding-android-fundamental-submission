package com.artworkspace.github

import android.content.Context
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class Utils {
    companion object {
        fun CircleImageView.setImageGlide(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .into(this)
        }
    }
}