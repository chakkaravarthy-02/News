package com.example.news.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.news.database.NewsDatabase
import com.example.news.database.asDomainModel
import com.example.news.domain.News
import com.example.news.network.NewsNetwork
import com.example.news.network.asDatabaseModel
import com.example.news.network.weather.WeatherItem
import com.example.news.network.weather.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(private val database: NewsDatabase) {
    val news :LiveData<List<News>> = database.newsDao.getNewsDetail().map {
        it.asDomainModel()
    }

    suspend fun deleteOld(){
        withContext(Dispatchers.IO){
            database.newsDao.deleteData(System.currentTimeMillis())
        }
    }
    suspend fun refreshNews(){
        withContext(Dispatchers.IO){
            try{
                val newsList = NewsNetwork.news.getNewsDetails().await()
                database.newsDao.upsertAll(*newsList.articles.asDatabaseModel())
            }catch (e: Throwable){
                println("Server error$e")
            }
        }
    }
}

class WeatherRepository {
    suspend fun refreshWeather(location: Location): WeatherItem? {
        var getWeatherDetail: WeatherItem? = null
        withContext(Dispatchers.IO) {
            try {
                getWeatherDetail = WeatherNetwork.weather.getWeatherDetails(
                    location.latitude,
                    location.longitude,
                    "minutely,hourly,daily,alerts",
                    "metric",
                    ""//apikey
                ).await()
            } catch (e: Exception) {
                println("Weather api error $e")
            }
        }
        return getWeatherDetail
    }
}