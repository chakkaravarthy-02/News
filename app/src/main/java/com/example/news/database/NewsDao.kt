package com.example.news.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.news.network.Article

@Dao
interface NewsDao{
    @Query("select * from databasenews")
    fun getNewsDetail(): LiveData<List<DatabaseNews>>

    @Upsert
    fun upsertAll(vararg news: DatabaseNews)

    @Query("delete from databasenews where (:currentTime - storedTime > 86400000)")
    fun deleteData(currentTime: Long)
}