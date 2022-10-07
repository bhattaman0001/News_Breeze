package com.example.newsbreeze

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class BookMarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_mark)

//        var map: HashMap<ArrayList<News>, Int> = HashMap<ArrayList<News>, Int>()
//        map = intent.getSerializableExtra("map") as HashMap<ArrayList<News>, Int>

    }

    fun goToMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}