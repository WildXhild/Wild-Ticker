package com.wildticker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
