package com.example.newsbreeze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.example.newsbreeze.databinding.ActivityDetailedBinding

class DetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedBinding
    private var extras: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        extras = intent.extras
        binding.bookMarkButton.setOnClickListener {
            val intent = Intent(this, BookMarkActivity::class.java)
            val bundle = Bundle()
            bundle.putString("Author", extras?.getString("Author"))
            bundle.putString("Title", extras?.getString("Title"))
            bundle.putString("Content", extras?.getString("Content"))
            bundle.putString("URL", extras?.getString("URL"))
            bundle.putString("Desc", extras?.getString("Desc"))
            bundle.putString("url", extras?.getString("url"))
            intent.putExtras(bundle)
            Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show()
        }
        binding.titleArticle.text = extras?.get("Title").toString()
        binding.content.text = extras?.get("Content").toString()
        binding.author.text = extras?.get("Author").toString()
        Glide.with(this).load(extras?.get("URL").toString()).into(binding.imageArticle)
        binding.button.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(extras?.get("url").toString()))
        }


    }

    fun goToMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}