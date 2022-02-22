package com.artworkspace.github.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.artworkspace.github.R
import com.artworkspace.github.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                finish()
            }
        }
    }
}