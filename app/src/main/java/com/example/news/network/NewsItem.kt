package com.example.news.network

data class NewsItem(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)