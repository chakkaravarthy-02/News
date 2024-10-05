package com.example.news.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DatabaseNews::class], version = 5, exportSchema = false)
abstract class NewsDatabase :RoomDatabase(){
    abstract val newsDao: NewsDao
}

private lateinit var INSTANCE : NewsDatabase
fun getDatabase(context: Context): NewsDatabase{
    synchronized(NewsDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                NewsDatabase::class.java,
                "News")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}