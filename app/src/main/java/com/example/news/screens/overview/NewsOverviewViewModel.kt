package com.example.news.screens.overview

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news.database.getDatabase
import com.example.news.domain.News
import com.example.news.network.weather.WeatherItem
import com.example.news.repository.NewsRepository
import com.example.news.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NewsOverviewViewModel(app: Application) : AndroidViewModel(app) {

    private val _navigateToWeb = MutableLiveData<String?>()
    val navigateToWeb: LiveData<String?>
        get() = _navigateToWeb

    val searchText = MutableLiveData<String?>()

    private val _weatherResponse = MutableLiveData<WeatherItem>()
    val weatherResponse : LiveData<WeatherItem>
        get() = _weatherResponse

    fun newsClicked(url: String) {
        _navigateToWeb.value = url
    }

    fun newsClickedComplete() {
        _navigateToWeb.value = null
    }

    fun filterText(query: String?): List<News>?{
        return response.value?.filter {
            it.title.lowercase().contains(query?.lowercase()?.trim() ?: " ") ||
                    it.description?.lowercase()?.contains(query?.lowercase()?.trim() ?: " ") ?: true
        }
    }

    private val job = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    private val database = getDatabase(app)
    private val repository = NewsRepository(database)

    private val weatherRepository = WeatherRepository()

    init {
        viewModelScope.launch {
            repository.refreshNews()
            repository.deleteOld()
        }
    }

    val response = repository.news

    fun getWeatherDetailProperty(location: Location) {
        viewModelScope.launch {
            _weatherResponse.value = weatherRepository.refreshWeather(location)
        }
    }
}