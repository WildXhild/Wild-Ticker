package com.wildticker.network

import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoApiService {
    // Coinfeeds-style price endpoint
    @GET("v1/coins/{symbol}/price")
    suspend fun getPrice(@Path("symbol") symbol: String): Map<String, Any>
}
