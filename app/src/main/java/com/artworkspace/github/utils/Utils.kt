package com.artworkspace.github.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import com.artworkspace.github.BuildConfig
import com.artworkspace.github.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class Utils {
    companion object {
        const val TOKEN = BuildConfig.API_KEY

        /**
         * Extension function to set CircleImageView using Glide
         *
         * @param context   Context
         * @param url   Image URL
         * @return Unit
         */
        fun CircleImageView.setImageGlide(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .into(this)
        }

        /**
         * Extension function to set and show hidden TextView if the text has value
         *
         * @param text  Text to set
         * @return Unit
         */
        fun TextView.setAndVisible(text: String?) {
            if (!text.isNullOrBlank()) {
                this.text = text
                this.visibility = View.VISIBLE
            }
        }
    }
}