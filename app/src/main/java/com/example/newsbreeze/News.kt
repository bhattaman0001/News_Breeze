package com.example.newsbreeze

class News {
    private var title: String = ""
    private var content: String = ""
    private var imageUrl: String = ""
    private var url: String = ""
    private var description: String = ""
    private var author: String = ""
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