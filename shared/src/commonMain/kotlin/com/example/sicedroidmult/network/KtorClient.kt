// ============================================================
// shared/commonMain/kotlin/.../network/KtorClient.kt
// Reemplaza: Retrofit + OkHttp + AddCookiesInterceptor + ReceivedCookiesInterceptor
// Los cookies se manejan automáticamente con HttpCookies plugin
// ============================================================
package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

private const val BASE_URL = "https://sicenet.surguanajuato.tecnm.mx"

// HttpClient compartido — equivalente a tu OkHttpClient + Retrofit
// expect/actual para que cada plataforma use su motor HTTP
expect fun createHttpClient(cookieStorage: CookiesStorage): HttpClient

// CookieStorage persistente — equivalente a tus SharedPreferences de cookies
// Se implementa por plataforma con expect/actual
expect fun createCookieStorage(): CookiesStorage

// Cliente listo para usar en el Repository
val cookieStorage: CookiesStorage = createCookieStorage()
val httpClient: HttpClient = createHttpClient(cookieStorage)

// ============================================================
// Equivalente a cada método de tu interfaz SICENETWService
// En vez de @POST/@Headers de Retrofit, usamos funciones suspend
// ============================================================

suspend fun soapPost(soapAction: String, body: String): String {
    return httpClient.post("$BASE_URL/ws/wsalumnos.asmx") {
        header("Content-Type", "text/xml;charset=utf-8")
        header("SOAPAction", "http://tempuri.org/$soapAction")
        setBody(body)
    }.bodyAsText()
}

// Equivalente a: snApiService.acceso(...)
suspend fun acceso(matricula: String, password: String): String {
    return soapPost("accesoLogin", getBodyAcceso(matricula, password))
}

// Equivalente a: snApiService.datos_alumno(...)
suspend fun datosAlumno(): String {
    return soapPost("getAlumnoAcademicoWithLineamiento", bodyPerfil)
}

// Equivalente a: snApiService.getCargaAcademica(...)
suspend fun getCargaAcademica(): String {
    return soapPost("getCargaAcademicaByAlumno", bodyCargaAcademica)
}

// Equivalente a: snApiService.getKardex(...)
suspend fun getKardex(lineamiento: Int): String {
    return soapPost("getAllKardexConPromedioByAlumno", getBodyKardex(lineamiento))
}

// Equivalente a: snApiService.getCalificacionesUnidad(...)
suspend fun getCalificacionesUnidad(): String {
    return soapPost("getCalifUnidadesByAlumno", bodyCalificacionFinal)
}

// Equivalente a: snApiService.getCalificacionesFinales(...)
suspend fun getCalificacionesFinales(): String {
    return soapPost("getAllCalifFinalByAlumnos", bodyCalificacionesUnidad)
}
