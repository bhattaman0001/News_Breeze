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

class NewsListAdapter(private val listener: NewsItemClicked) :
    RecyclerView.Adapter<NewsViewHolder>() {

    private val items: ArrayList<News> = ArrayList()
    var context : Context = TODO()

    // create view with the help of view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    // binds the view with view holder
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.author.text = currentItem.author
        holder.date.text = currentItem.date
        holder.description.text = currentItem.description
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)

        holder.constraintLayout.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", currentItem.title)
            bundle.putString("content", currentItem.content)
            bundle.putString("url", currentItem.imageUrl)
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
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.title)
    val image: ImageView = itemView.findViewById(R.id.imageView)
    val author: TextView = itemView.findViewById(R.id.author)
    val date: TextView = itemView.findViewById(R.id.date)
    val description: TextView = itemView.findViewById(R.id.description)
    val content: TextView = itemView.findViewById(R.id.content)
    val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.itemsLayout)
}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}

