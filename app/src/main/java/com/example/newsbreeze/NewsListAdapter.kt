package com.example.newsbreeze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// layout inflater converts an xml into view format

class NewsListAdapter(private val context: Context) :
    RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    private val items: ArrayList<News> = ArrayList()

    // create view with the help of view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    // binds the view with view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.author.text = currentItem.author
        holder.date.text = currentItem.date
        holder.description.text = currentItem.description
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)

        holder.ll.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            val bundle = Bundle()
            bundle.putString("Author", currentItem.author)
            bundle.putString("Title", currentItem.title)
            bundle.putString("Content", currentItem.content)
            bundle.putString("URL", currentItem.imageUrl)
            bundle.putString("Desc", currentItem.description)
            bundle.putString("url", currentItem.url)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }


    }

    // tells how many items should be on screen at a time
    override fun getItemCount(): Int {
        return items.size
    }

    fun updateNews(updatedNews: ArrayList<News>) {
        items.clear()
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ll: ConstraintLayout = itemView.findViewById(R.id.ll)
        val titleView: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.imageView)
        val author: TextView = itemView.findViewById(R.id.author)
        val date: TextView = itemView.findViewById(R.id.date)
        val description: TextView = itemView.findViewById(R.id.description)
    }
}

//interface NewsItemClicked {
//    fun onItemClicked(item: News)
//}

