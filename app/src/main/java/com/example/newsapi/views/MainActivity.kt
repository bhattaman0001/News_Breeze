package com.example.newsapi.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapi.NewsApplication
import com.example.newsapi.R
import com.example.newsapi.repository.HeadlinesRepository
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.viewmodels.MainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var headlinesRepository: HeadlinesRepository
    lateinit var bottomNavigaionView: BottomNavigationView
    override fun onStart() {
        super.onStart()
        headlinesRepository = (application as NewsApplication).repository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(application, headlinesRepository)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigaionView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigaionView.setupWithNavController(navController)
    }
}