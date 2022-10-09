package com.example.newsbreeze

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsbreeze.databinding.ActivityBookMarkBinding

class BookMarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookMarkBinding
    private var extras: Bundle? = null
    private lateinit var mAdapter: NewsListAdapter
    private var newsArray = ArrayList<News>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewBookMark.layoutManager = LinearLayoutManager(this)
        mAdapter = NewsListAdapter(this)
        binding.recyclerViewBookMark.adapter = mAdapter

        extras = intent.extras
        val news = News()
        extras?.getString("Title")?.let { news.setTitle(it) }
        extras?.getString("Author")?.let { news.setAuthor(it) }
        extras?.getString("url")?.let { news.setUrl(it) }
        extras?.getString("URL")?.let { news.setImageUrl(it) }
        extras?.getString("date")?.let { news.setDate(it) }
        extras?.getString("Desc")?.let { news.setDescription(it) }
        extras?.getString("Content")?.let { news.setContent(it) }
        newsArray.add(news)
        mAdapter.updateNews(newsArray)
    }

    fun goToMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}