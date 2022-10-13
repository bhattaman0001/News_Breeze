package com.example.newsapi.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapi.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticle(article: Article): Long

    @Query("SELECT * FROM ARTICLES")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}