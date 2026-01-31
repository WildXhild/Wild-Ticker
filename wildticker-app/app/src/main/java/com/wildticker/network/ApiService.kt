package com.wildticker.network

import com.wildticker.data.NewsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // News headlines by category
    @GET("v1/news")
    suspend fun getHeadlines(
        @Query("category") category: String?
    ): List<NewsItem>

    // Sports live scores (example)
    @GET("v1/sports/live/{sport}")
    suspend fun getLiveScores(@Path("sport") sport: String): List<Map<String, Any>>

    // Crypto prices
    @GET("v1/crypto/{symbol}/price")
    suspend fun getCryptoPrice(@Path("symbol") symbol: String): Map<String, Any>

    // Stock quotes
    @GET("v1/stocks/{symbol}/quote")
    suspend fun getStockQuote(@Path("symbol") symbol: String): Map<String, Any>
}
