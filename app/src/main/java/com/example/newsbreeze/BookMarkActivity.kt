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
        var news = News()
        news.setTitle(extras?.get("Title").toString())
        news.setAuthor(extras?.get("Author").toString())
        news.setUrl(extras?.get("url").toString())
        news.setImageUrl(extras?.get("URL").toString())
        news.setDate(extras?.get("date").toString())
        news.setDescription(extras?.get("Desc").toString())
        news.setContent(extras?.get("Content").toString())
        newsArray.add(news)
        mAdapter.updateNews(newsArray)
    }

    fun goToMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}