// ============================================================
// shared/iosMain/kotlin/.../network/KtorClient.ios.kt
// Motor HTTP: Darwin (NSURLSession nativo de iOS)
// ============================================================
package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.cookies.*

import io.ktor.client.plugins.logging.*

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(Darwin) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
        // Desactivamos followRedirects para que la lógica manual de soapPost funcione en iOS
        followRedirects = false
    }
}

actual fun createCookieStorage(): CookiesStorage {
    return AcceptAllCookiesStorage()
}
