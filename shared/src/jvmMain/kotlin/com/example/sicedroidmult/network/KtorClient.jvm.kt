package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.cookies.*

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(Java) {
        install(HttpCookies) {
            storage = cookieStorage
        }
    }
}

actual fun createCookieStorage(): CookiesStorage {
    // En Desktop podemos usar memoria o persistir en un archivo si fuera necesario
    return AcceptAllCookiesStorage()
}
