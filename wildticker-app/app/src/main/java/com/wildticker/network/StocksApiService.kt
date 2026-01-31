package com.wildticker.network

import retrofit2.http.GET
import retrofit2.http.Path

interface StocksApiService {
    // Finage-style quote endpoint
    @GET("v1/quote/{symbol}")
    suspend fun getQuote(@Path("symbol") symbol: String): Map<String, Any>
}
