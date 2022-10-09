package com.example.newsbreeze.architecture

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsbreeze.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: News)

    @Query("SELECT * FROM News")
    suspend fun getNewsFromDatabase(): LiveData<ArrayList<News>>

    @Delete
    suspend fun deleteNews(news: News)
}