// ============================================================
// shared/androidMain/kotlin/.../network/KtorClient.android.kt
// Motor HTTP: OkHttp (mismo que usaba Retrofit)
// CookieStorage: persiste en SharedPreferences (= tus interceptors)
// ============================================================
package com.example.sicedroidmult.network

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cookies.*

// El contexto se asigna desde DefaultAppContainer antes de usar el cliente
lateinit var androidContext: Context

actual fun createHttpClient(cookieStorage: CookiesStorage): HttpClient {
    return HttpClient(OkHttp) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        // Aquí puedes añadir timeouts, logging, etc.
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
