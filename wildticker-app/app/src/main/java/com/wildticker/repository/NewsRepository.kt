package com.wildticker.repository

import com.wildticker.data.NewsDao
import com.wildticker.data.NewsItem
import com.wildticker.network.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val api: NewsApiService,
    private val dao: NewsDao
) {
    suspend fun refresh(category: String?) {
        // Fetch DTOs from provider and map to NewsItem
        val dtos = api.getHeadlines(category)
        val items = dtos.map { it.toNewsItem() }
        withContext(Dispatchers.IO) {
            dao.upsert(items)
            dao.purgeOlderThan(System.currentTimeMillis() - 1000L * 60 * 60 * 24)
        }
    }

    fun cached(): Flow<List<NewsItem>> = dao.getAll().flowOn(Dispatchers.IO)
}
