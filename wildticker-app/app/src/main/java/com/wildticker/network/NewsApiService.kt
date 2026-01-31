package com.wildticker.network

import retrofit2.http.GET
import retrofit2.http.Query

// DTO for Feedrika-like responses will be declared separately.
import com.wildticker.network.NewsDto

interface NewsApiService {
    // Feedrika-style headlines endpoint returning DTOs
    @GET("v1/headlines")
    suspend fun getHeadlines(@Query("category") category: String?): List<NewsDto>
}
