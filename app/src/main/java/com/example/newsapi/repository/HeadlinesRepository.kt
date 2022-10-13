package com.example.newsapi.repository

import com.example.newsapi.api.RetrofitHelper
import com.example.newsapi.db.ArticleDatabase
import com.example.newsapi.model.Article

class HeadlinesRepository(
    val db: ArticleDatabase
) {

    suspend fun getBreakingNews(country: String, pageNumber: Int) =
        RetrofitHelper.api.getBreakingNews(country, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitHelper.api.searchNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsertArticle(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)


}