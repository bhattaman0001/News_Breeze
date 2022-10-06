package com.example.newsbreeze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide


class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val imageArticle : ImageView = findViewById(R.id.imageArticle)
        val titleArticle : TextView = findViewById(R.id.titleArticle)
        val contentArticle : TextView = findViewById(R.id.content)

        val bundle = intent.extras

        val title = bundle!!.getString("title").toString()
        val content = bundle.getString("content").toString()
        val url = bundle.getString("url").toString()

        Glide.with(this).load(url).into(imageArticle)
        titleArticle.text = title
        contentArticle.text = content
    }

    fun goToMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun goToChrome(news: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(news.url))
    }
}