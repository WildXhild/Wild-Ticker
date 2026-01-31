package com.wildticker.network

import retrofit2.http.GET
import retrofit2.http.Path

interface SportsApiService {
    // Example sportmonks-style live scores
    @GET("v2/fixtures/live/{sport}")
    suspend fun getLive(@Path("sport") sport: String): List<Map<String, Any>>
}
