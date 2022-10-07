package com.example.newsbreeze

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsbreeze.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding

    // those variables which are initialized after the declaration
    private var newsArray = ArrayList<News>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()
        mAdapter = NewsListAdapter(this)
        binding.recyclerView.adapter = mAdapter

//        binding.search.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                filter(s.toString())
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
    }

    private fun filter(text: String) {
        val filteredlist = ArrayList<News>()
        for (i in newsArray) {
            if (i.author.lowercase(Locale.ROOT).contains(text.lowercase(Locale.getDefault()))) {
                filteredlist.add(i)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            mAdapter.updateNews(filteredlist)
        }
    }

    private fun fetchData() {
//        val url = "https://saurav.tech/NewsAPI/everything/cnn.json";
        val url = "https://saurav.tech/NewsAPI/everything/bbc-news.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
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
    }
}