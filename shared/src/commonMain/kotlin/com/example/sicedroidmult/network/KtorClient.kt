package com.example.sicedroidmult.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
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

/**
 * Realiza una petición POST de tipo SOAP.
 */
suspend fun soapPost(soapAction: String, body: String): String {
    val url = "$BASE_URL/ws/wsalumnos.asmx?AspxAutoDetectCookieSupport=1"
    
    val response = httpClient.post(url) {
        header("Content-Type", "text/xml;charset=utf-8")
        header("SOAPAction", "http://tempuri.org/$soapAction")
        header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        setBody(body)
    }

    if (response.status == HttpStatusCode.Found || response.status == HttpStatusCode.MovedPermanently) {
        var nextUrl = response.headers["Location"] ?: url
        
        // Corrección para servidores que redirigen erróneamente a localhost o HTTP
        if (nextUrl.startsWith("http://localhost") || nextUrl.startsWith("/")) {
            nextUrl = "$BASE_URL/ws/wsalumnos.asmx?AspxAutoDetectCookieSupport=1"
        } else if (nextUrl.startsWith("http://") && BASE_URL.startsWith("https://")) {
            nextUrl = nextUrl.replace("http://", "https://")
        }

        println("DEBUG: Redirigiendo manualmente a $nextUrl manteniendo el POST")
        
        return httpClient.post(nextUrl) {
            header("Content-Type", "text/xml;charset=utf-8")
            header("SOAPAction", "http://tempuri.org/$soapAction")
            header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            setBody(body)
        }.bodyAsText()
    }

    return response.bodyAsText()
}

/**
 * Equivalente a: snApiService.acceso(...)
 */
suspend fun networkAcceso(matricula: String, password: String): String {
    val body = bodyacceso
        .replaceFirst("%s", matricula)
        .replaceFirst("%s", password)
    return soapPost("accesoLogin", body)
}

/**
 * Equivalente a: snApiService.datos_alumno(...)
 */
suspend fun networkDatosAlumno(): String {
    return soapPost("getAlumnoAcademicoWithLineamiento", bodyPerfil)
}

/**
 * Equivalente a: snApiService.getCargaAcademica(...)
 */
suspend fun networkGetCargaAcademica(): String {
    return soapPost("getCargaAcademicaByAlumno", CargaAcademicaItem)
}

/**
 * Equivalente a: snApiService.getKardex(...)
 */
suspend fun networkGetKardex(lineamiento: Int): String {
    val body = KardexItem.replace("%d", lineamiento.toString())
    return soapPost("getAllKardexConPromedioByAlumno", body)
}

/**
 * Equivalente a: snApiService.getCalificacionesUnidad(...)
 */
suspend fun networkGetCalificacionesUnidad(): String {
    // Nota: Parece que el XML en bodyacceso para esto se llama CalificacionFinalItem por error?
    // Revisando bodyacceso.kt: CalificacionFinalItem contiene getCalifUnidadesByAlumno
    return soapPost("getCalifUnidadesByAlumno", CalificacionFinalItem)
}

/**
 * Equivalente a: snApiService.getCalificacionesFinales(...)
 */
suspend fun networkGetCalificacionesFinales(modEducativo: Int): String {
    // Revisando bodyacceso.kt: CalificacionesUnidadItem contiene getAllCalifFinalByAlumnos
    val body = CalificacionesUnidadItem.replace("unsignedByte", modEducativo.toString())
    return soapPost("getAllCalifFinalByAlumnos", body)
}
