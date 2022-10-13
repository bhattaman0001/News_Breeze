package com.example.newsapi.views

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapi.R

class WebViewActivity : AppCompatActivity() {
    lateinit var browser: WebView
    lateinit var url: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        url = intent.getStringExtra("url").toString()
        intent.extras!!.getSerializable("article")
        browser = findViewById(R.id.webview)

        browser.webViewClient = MyBrowser()
        browser.getSettings().setLoadsImagesAutomatically(true)
        browser.getSettings().setJavaScriptEnabled(true)
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        browser.loadUrl(url)

    }

    private class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

}

