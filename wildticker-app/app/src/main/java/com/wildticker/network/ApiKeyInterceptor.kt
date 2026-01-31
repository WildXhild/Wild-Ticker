package com.wildticker.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
    private val keyName: String,
    private val keyValue: String,
    private val useHeader: Boolean = false
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        return if (useHeader) {
            val newReq = req.newBuilder()
                .addHeader(keyName, keyValue)
                .build()
            chain.proceed(newReq)
        } else {
            val url: HttpUrl = req.url.newBuilder()
                .addQueryParameter(keyName, keyValue)
                .build()
            val newReq = req.newBuilder().url(url).build()
            chain.proceed(newReq)
        }
    }
}
