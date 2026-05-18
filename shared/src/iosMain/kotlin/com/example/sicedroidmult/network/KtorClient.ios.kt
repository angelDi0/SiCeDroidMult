package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.cookies.*

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(Darwin) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }
}

actual fun createCookieStorage(): CookiesStorage {
    return AcceptAllCookiesStorage()
}
