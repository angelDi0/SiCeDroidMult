package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.header

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(OkHttp) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(HttpTimeout) {
            requestTimeoutMillis  = 15_000
            connectTimeoutMillis  = 10_000
            socketTimeoutMillis   = 15_000
        }
        install(DefaultRequest) {
            header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
            )
        }
    }
}

actual fun createCookieStorage(): CookiesStorage {
    return AcceptAllCookiesStorage()
}
