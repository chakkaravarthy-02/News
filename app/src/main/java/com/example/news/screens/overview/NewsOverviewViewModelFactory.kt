package com.example.news.screens.overview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewsOverviewViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsOverviewViewModel::class.java)) {
            return NewsOverviewViewModel(app) as T
        } else {
            throw IllegalArgumentException("Unknown class")
        }
    }
}