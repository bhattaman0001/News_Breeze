package com.example.newsbreeze

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
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
//                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
//        intent = Intent(this, DetailedActivity::class.java)
//        startActivity(intent)
//        finish()
    }

    fun goToBookMark(view: View) {
        startActivity(Intent(this, BookMarkActivity::class.java))
        finish()
    }
}