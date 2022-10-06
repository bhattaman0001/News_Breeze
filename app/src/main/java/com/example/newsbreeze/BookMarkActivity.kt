package com.example.newsbreeze

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class BookMarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_mark)
    }

    fun goToMain(view : View){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}