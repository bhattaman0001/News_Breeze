package com.example.newsbreeze.architecture

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsbreeze.News

@Database(entities = [News::class], version = 3, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}