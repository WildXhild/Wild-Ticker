package com.wildticker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY timestamp DESC")
    fun getAll(): Flow<List<NewsItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(items: List<NewsItem>)

    @Query("DELETE FROM news WHERE timestamp < :threshold")
    suspend fun purgeOlderThan(threshold: Long)
}
