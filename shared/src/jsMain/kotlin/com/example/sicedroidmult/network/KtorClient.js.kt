package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.cookies.*

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(Js) {
        install(HttpCookies) {
            storage = cookieStorage
        }
    }
}

actual fun createCookieStorage(): CookiesStorage {
    // En JS (Browser) el navegador maneja las cookies automáticamente si no se especifica storage,
    // pero Ktor requiere uno para el plugin. AcceptAllCookiesStorage funciona bien.
    return AcceptAllCookiesStorage()
}
