package com.wildticker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsItem(
    @PrimaryKey val id: String,
    val title: String,
    val url: String?,
    val source: String?,
    val category: String?,
    val timestamp: Long
)
