package com.example.sicedroidmult.network

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cookies.*

import io.ktor.client.plugins.logging.*

// El contexto se asigna desde DefaultAppContainer antes de usar el cliente
lateinit var androidContext: Context

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(OkHttp) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
        // DESACTIVAMOS followRedirects para evitar que convierta POST en GET
        followRedirects = false
        engine {
            config {
                followRedirects(false)
            }
        }
    }
}

// Reemplaza AddCookiesInterceptor + ReceivedCookiesInterceptor
// Ktor maneja automáticamente Set-Cookie y Cookie headers
actual fun createCookieStorage(): CookiesStorage {
    // Usamos AcceptAllCookiesStorage en memoria
    // Si necesitas persistencia entre reinicios de app, implementa
    // CookiesStorage guardando en SharedPreferences de androidContext
    return AcceptAllCookiesStorage()
}
