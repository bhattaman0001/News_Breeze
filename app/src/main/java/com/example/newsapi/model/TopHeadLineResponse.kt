package com.example.newsapi.model

data class TopHeadLineResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)