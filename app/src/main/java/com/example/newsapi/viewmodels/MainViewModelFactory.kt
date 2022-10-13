package com.example.newsapi.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.repository.HeadlinesRepository

class MainViewModelFactory(
    val app: Application, private val repository: HeadlinesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(app, repository) as T
    }
}