package com.wildticker.network

import com.wildticker.data.NewsItem

// Example DTO matching common headline API fields.
// Adjust fields to match provider JSON exactly.
data class NewsDto(
    val id: String?,
    val title: String?,
    val url: String?,
    val source: String?,
    val category: String?,
    val published_at: Long?
) {
    fun toNewsItem(): NewsItem {
        val ts = published_at ?: System.currentTimeMillis()
        val nid = id ?: url ?: title ?: ts.toString()
        return NewsItem(
            id = nid,
            title = title ?: "(no title)",
            url = url,
            source = source,
            category = category,
            timestamp = ts
        )
    }
}
