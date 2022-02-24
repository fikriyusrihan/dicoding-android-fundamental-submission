package com.artworkspace.github

import android.content.Context
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class Utils {
    companion object {
        const val TOKEN = "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"

        fun CircleImageView.setImageGlide(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .into(this)
        }
    }
}