package com.example.newsapi.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapi.R
import com.example.newsapi.model.Article
import com.example.newsapi.views.WebViewActivity

class HeadineAdapter(
    private val context: Context
) : RecyclerView.Adapter<HeadineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.headline_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.tvSource.text = article.source?.name
        holder.tvDescription.text = article.description
        holder.tvPublishedAt.text = article.publishedAt
        holder.tvTitle.text = article.title
        Glide.with(holder.ivArticleImage).load(article.urlToImage).into(holder.ivArticleImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", article.url)
            context.startActivity(intent)

        }
        holder.itemView.setOnClickListener {
            onItemClickListener.let {
                if (it != null) {
                    it(article)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItenClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(
        itemView: View,
        var tvSource: TextView = itemView.findViewById(R.id.tvSource),
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle),
        var tvDescription: TextView = itemView.findViewById(R.id.tvDescription),
        var tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt),
        var ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
    ) : RecyclerView.ViewHolder(itemView)

    private val differcallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differcallback)
}