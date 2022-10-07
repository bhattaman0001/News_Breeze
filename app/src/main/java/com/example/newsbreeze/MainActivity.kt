package com.example.newsbreeze

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsbreeze.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()
        mAdapter = NewsListAdapter(this)
        binding.recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJSONObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJSONObject.getString("title"),
                        newsJSONObject.getString("author"),
                        newsJSONObject.getString("url"),
                        newsJSONObject.getString("urlToImage"),
                        newsJSONObject.getString("publishedAt"),
                        newsJSONObject.getString("description"),
                        newsJSONObject.getString("content")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun goToBookMark(view: View) {
//        val intent = Intent(this, BookMarkActivity::class.java)
//        intent.putExtra("map", map)
        Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show()
//        finish()
    }

    fun goToBookMarkActivity(view: View) {
        startActivity(Intent(this, BookMarkActivity::class.java))
        finish()
    }
}