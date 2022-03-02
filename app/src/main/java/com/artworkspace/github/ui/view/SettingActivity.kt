package com.artworkspace.github.ui.view

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.artworkspace.github.R
import com.artworkspace.github.databinding.ActivitySettingBinding
import com.artworkspace.github.ui.viewmodel.SettingViewModel
import com.artworkspace.github.ui.viewmodel.ViewModelFactory

class SettingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar(getString(R.string.setting))

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            binding.switchDarkMode.isChecked = isDarkModeActive
        }

        binding.switchDarkMode.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.switch_dark_mode -> settingViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * Setting up toolbar
     *
     * @param title Toolbar title
     * @return Unit
     */
    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbarSetting)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }


}