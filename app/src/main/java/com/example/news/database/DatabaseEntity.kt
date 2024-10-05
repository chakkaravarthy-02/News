package com.example.news.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.news.domain.News
import com.example.news.network.Article
import com.example.news.network.Source


@Entity
data class DatabaseNews (
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val storedTime: Long = System.currentTimeMillis(),
    val title: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String?
)

fun List<DatabaseNews>.asDomainModel(): List<News>{
    return map{
        News(author = it.author, content = it.content, description = it.description, publishedAt = it.publishedAt, title = it.title, url = it.url, urlToImage = it.urlToImage)
    }
}