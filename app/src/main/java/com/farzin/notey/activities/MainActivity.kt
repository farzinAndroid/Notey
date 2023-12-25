package com.farzin.notey.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.farzin.notey.databinding.ActivityMainBinding
import com.farzin.notey.data.db.NoteDatabase
import com.farzin.notey.repo.NoteRepo
import com.farzin.notey.utils.Constants
import com.farzin.notey.utils.LocaleUtils
import com.farzin.notey.utils.appConfig
import com.farzin.notey.viewmodel.DataStoreViewModel
import com.farzin.notey.viewmodel.NoteActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        appConfig(dataStoreViewModel)
        LocaleUtils.setLocale(this,Constants.USER_LANG)
    }
}