package com.example.newsapi.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapi.R

class WebViewActivity : AppCompatActivity() {
    private lateinit var browser: WebView
    private lateinit var url: String
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        url = intent.getStringExtra("url").toString()
        intent.extras!!.getSerializable("article")
        browser = findViewById(R.id.webview)

        browser.webViewClient = MyBrowser()
        browser.settings.loadsImagesAutomatically = true
        browser.settings.javaScriptEnabled = true
        browser.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        browser.loadUrl(url)

    }

    private class MyBrowser : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

}

