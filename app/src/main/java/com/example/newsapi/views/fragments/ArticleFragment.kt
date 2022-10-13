package com.example.newsapi.views.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapi.R
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.views.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: MainViewModel
    val arg: ArticleFragmentArgs by navArgs()
    lateinit var webView: WebView
    lateinit var fab: FloatingActionButton
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel
        val article = arg.article
        webView = view.findViewById(R.id.webView)
        fab = view.findViewById(R.id.fab)

        webView.apply {
            webViewClient = MyBrowser()
            settings.loadsImagesAutomatically
            settings.javaScriptEnabled
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            loadUrl(article.url.toString())
        }
        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article Saved Sucessfully", Snackbar.LENGTH_SHORT).show()
        }
    }

    private class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

}