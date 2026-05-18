// ============================================================
// shared/iosMain/kotlin/.../network/KtorClient.ios.kt
// Motor HTTP: Darwin (NSURLSession nativo de iOS)
// ============================================================
package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.cookies.*

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(Darwin) {
        install(HttpCookies) {
            storage = cookieStorage
        }
    }
}

actual fun createCookieStorage(): CookiesStorage {
    return AcceptAllCookiesStorage()
}
