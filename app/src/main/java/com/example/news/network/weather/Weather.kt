package com.example.news.network.weather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)