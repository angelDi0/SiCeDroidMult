package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(Java) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
        // Desactivamos followRedirects para que la lógica manual de soapPost funcione en Desktop
        followRedirects = false
    }
}

actual fun createCookieStorage(): CookiesStorage {
    return AcceptAllCookiesStorage()
}
