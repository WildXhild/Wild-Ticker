package com.wildticker.di

import com.wildticker.network.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val FEEDRIKA_BASE = "https://api.feedrika.example/"
    private const val SPORTMONKS_BASE = "https://api.sportmonks.example/"
    private const val COINFEEDS_BASE = "https://api.coinfeeds.example/"
    private const val FINAGE_BASE = "https://api.finage.example/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private fun baseOkHttp(keyName: String?, keyValue: String?, useHeader: Boolean): OkHttpClient {
        val b = OkHttpClient.Builder()
        if (!keyName.isNullOrEmpty() && !keyValue.isNullOrEmpty()) {
            b.addInterceptor(com.wildticker.network.ApiKeyInterceptor(keyName, keyValue, useHeader))
        }
        return b.build()
    }

    private fun buildRetrofit(baseUrl: String, moshi: Moshi, ok: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(ok)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(moshi: Moshi): com.wildticker.network.NewsApiService {
        val ok = baseOkHttp("api_key", com.wildticker.BuildConfig.FEEDRIKA_API_KEY, false)
        val base = com.wildticker.BuildConfig.FEEDRIKA_BASE_URL
        return buildRetrofit(base, moshi, ok).create(com.wildticker.network.NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSportsApi(moshi: Moshi): com.wildticker.network.SportsApiService {
        // Sportmonks typically uses a query parameter like "api_token"
        val ok = baseOkHttp("api_token", com.wildticker.BuildConfig.SPORTMONKS_API_KEY, false)
        val base = com.wildticker.BuildConfig.SPORTMONKS_BASE_URL
        return buildRetrofit(base, moshi, ok).create(com.wildticker.network.SportsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCryptoApi(moshi: Moshi): com.wildticker.network.CryptoApiService {
        // Using CoinGecko which is free and needs no key
        val ok = baseOkHttp(null, null, false)
        val base = com.wildticker.BuildConfig.COINFEEDS_BASE_URL
        return buildRetrofit(base, moshi, ok).create(com.wildticker.network.CryptoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStocksApi(moshi: Moshi): com.wildticker.network.StocksApiService {
        // Finage accepts apiKey as query parameter named "apiKey" or header; using query here
        val ok = baseOkHttp("apiKey", com.wildticker.BuildConfig.FINAGE_API_KEY, false)
        val base = com.wildticker.BuildConfig.FINAGE_BASE_URL
        return buildRetrofit(base, moshi, ok).create(com.wildticker.network.StocksApiService::class.java)
    }
}
