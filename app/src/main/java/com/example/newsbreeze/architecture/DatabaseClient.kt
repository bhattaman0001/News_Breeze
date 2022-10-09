package com.example.newsbreeze.architecture

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room

class DatabaseClient(private var context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var mInstance: DatabaseClient
    }

    private lateinit var newsDatabase: NewsDatabase

    @SuppressLint("NotConstructor")
    fun DatabaseClient() {
        newsDatabase = Room.databaseBuilder(context, NewsDatabase::class.java, "alldata").build()
    }

    fun getInstance(context: Context): DatabaseClient {
        if (mInstance == null) {
            mInstance = DatabaseClient(context)
        }
        return mInstance
    }

    fun getNewsDatabase(): NewsDatabase {
        return newsDatabase
    }
}