package com.example.newsapi.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapi.NewsApplication
import com.example.newsapi.model.Article
import com.example.newsapi.model.TopHeadLineResponse
import com.example.newsapi.repository.HeadlinesRepository
import com.example.newsapi.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel(
    app: Application, private val repository: HeadlinesRepository
) : AndroidViewModel(app) {

    private val breakingNews: MutableLiveData<Resource<TopHeadLineResponse>> = MutableLiveData()
    var breakingNewsPageNumber = 1

    private val _searchNews: MutableLiveData<Resource<TopHeadLineResponse>> = MutableLiveData()
    private var searchNewsPageNumber = 1
    private var breakingNewsResponse: TopHeadLineResponse? = null

    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(country: String) = viewModelScope.launch {
        safeBreakingNewsCall(country)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleSearchNews(response: Response<TopHeadLineResponse>): Resource<TopHeadLineResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleBreakingNews(response: Response<TopHeadLineResponse>): Resource<TopHeadLineResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPageNumber++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews(): LiveData<List<Article>> = repository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(country: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                val response = repository.getBreakingNews(country, breakingNewsPageNumber)
                breakingNews.postValue(handleBreakingNews(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection Available"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        _searchNews.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                val response = repository.searchNews(searchQuery, searchNewsPageNumber)
                _searchNews.postValue(handleSearchNews(response))
            } else {
                _searchNews.postValue(Resource.Error("No Internet Connection Available"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchNews.postValue(Resource.Error("Network Failiure"))
                else -> _searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) connectivityManager.getNetworkCapabilities(
                connectivityManager.activeNetwork
            ) else connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    val bNews: LiveData<Resource<TopHeadLineResponse>>
        get() = breakingNews
    val searchNews: LiveData<Resource<TopHeadLineResponse>>
        get() = _searchNews


}