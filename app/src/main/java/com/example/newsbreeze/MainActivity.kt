package com.example.newsbreeze

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsbreeze.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding

    // those variables which are initialized after the declaration
    private var newsArray = ArrayList<News>()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()
        mAdapter = NewsListAdapter(this)
        binding.recyclerView.adapter = mAdapter

        Spinner()

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString().lowercase(Locale.ROOT))
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.refreshLayout.setOnRefreshListener {
            val text = "Sort By"
            val editable = Editable.Factory.getInstance().newEditable(text)
            binding.autoCompleteTextView.text = editable
            Spinner()
            binding.search.text = null
            mAdapter.updateNews(newsArray)
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun Spinner() {
        val options = arrayOf("sortByDate", "sortByTitle")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setOnItemClickListener(this)
    }

    private fun filter(text: String) {
        val filteredlist = ArrayList<News>()
        for (i in newsArray) {
            var str = ""
            for (j in i.getTitle().toLowerCase()) {
                str += j
                if (str.length <= text.length) {
                    if (str in text) {
                        filteredlist.add(i)
                    }
                }
            }
            mAdapter.updateNews(filteredlist)
        }
        if (filteredlist.isEmpty()) {
            mAdapter.updateNews(newsArray)
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
                    val news = News()
                    news.setTitle(newsJSONObject.getString("title"))
                    news.setAuthor(newsJSONObject.getString("author"))
                    news.setUrl(newsJSONObject.getString("url"))
                    news.setImageUrl(newsJSONObject.getString("urlToImage"))
                    news.setDate(newsJSONObject.getString("publishedAt"))
                    news.setDescription(newsJSONObject.getString("description"))
                    news.setContent(newsJSONObject.getString("content"))
                    newsArray.add(news)
                }
                newsArray.distinct().toList()
                mAdapter.updateNews(newsArray)
            },
            {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun goToBookMarkActivity(view: View) {
        startActivity(Intent(this, BookMarkActivity::class.java))
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                newsArray.sortedBy {
                    it.getDate()
                }
                Toast.makeText(this, "Sort by Date", Toast.LENGTH_SHORT).show()
                mAdapter.updateNews(newsArray)
                mAdapter.notifyDataSetChanged()
            }
            1 -> {
                newsArray.sortedBy {
                    it.getTitle()
                }
                Toast.makeText(this, "Sort by Title", Toast.LENGTH_SHORT).show()
                mAdapter.updateNews(newsArray)
                mAdapter.notifyDataSetChanged()
            }
        }
    }
}