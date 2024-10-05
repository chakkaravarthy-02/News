package com.example.news.network

import com.example.news.database.DatabaseNews

fun List<Article>.asDatabaseModel(): Array<DatabaseNews>{
    return map{
        DatabaseNews(author = it.author, content = it.content, description = it.description, publishedAt = it.publishedAt, title = it.title, url = it.url, urlToImage = it.urlToImage)
    }.toTypedArray()
}