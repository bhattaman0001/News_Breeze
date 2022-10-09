package com.example.newsbreeze

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class News {
    @ColumnInfo
    private var title: String = ""
    @ColumnInfo
    private var content: String = ""
    @ColumnInfo
    private var imageUrl: String = ""
    @ColumnInfo
    private var url: String = ""
    @ColumnInfo
    private var description: String = ""
    @ColumnInfo
    private var author: String = ""
    @ColumnInfo
    private var date: String = ""

//    class compareByDate : Comparator<News> {
//        override fun compare(p1: News?, p2: News?): Int {
//            if (p1 == null || p2 == null) return 0
//            return p1.getDate().compareTo(p2.getDate())
//        }
//    }

    fun getTitle(): String {
        return title
    }

    fun getImageUrl(): String {
        return imageUrl
    }

    fun getUrl(): String {
        return url
    }

    fun getDescription(): String {
        return description
    }

    fun getContent(): String {
        return content
    }

    fun getDate(): String {
        return date
    }

    fun getAuthor(): String {
        return author
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setImageUrl(imageUrl: String) {
        this.imageUrl = imageUrl
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setContent(content: String) {
        this.content = content
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun setAuthor(author: String) {
        this.author = author
    }

}