package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.cookies.*

import io.ktor.client.plugins.logging.*

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(Js) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
        // Desactivamos followRedirects para que la lógica manual de soapPost funcione en JS
        followRedirects = false
    }
}

actual fun createCookieStorage(): CookiesStorage {
    // En JS (Browser) el navegador maneja las cookies automáticamente si no se especifica storage,
    // pero Ktor requiere uno para el plugin. AcceptAllCookiesStorage funciona bien.
    return AcceptAllCookiesStorage()
}
