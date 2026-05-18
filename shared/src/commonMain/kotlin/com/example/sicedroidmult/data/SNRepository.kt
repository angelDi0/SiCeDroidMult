// ============================================================
// shared/commonMain/kotlin/.../data/SNRepository.kt
// Cambios respecto al original:
//   - Sin android.util.Log (no existe en commonMain)
//   - Sin .toRequestBody() de OkHttp
//   - Llama a las funciones de KtorClient.kt directamente
//   - Los DAOs ahora son queries de SQLDelight (AppDatabase)
// ============================================================
package com.example.sicedroidmult.data

import com.example.sicedroidmult.db.AppDatabase
import com.example.sicedroidmult.network.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface SNRepository {
    suspend fun acceso(m: String, p: String): String
    suspend fun datos_alumno(): String
    suspend fun getCargaAcademica(): String
    suspend fun getKardex(l: Int = 0): String
    suspend fun getCalificacionesUnidad(): String
    suspend fun getCalificacionesFinales(): String
}

// ---------------------------------------------------------------
// Implementación LOCAL — lee desde SQLDelight (= Room antes)
// ---------------------------------------------------------------
class DBLocalSNRepository(
    private val db: AppDatabase
) : SNRepository {

    override suspend fun acceso(m: String, p: String): String = "SUCCESS"

    override suspend fun datos_alumno(): String {
        // SQLDelight genera db.estudianteQueries.getPerfilSync()
        val estudiante = db.estudianteQueries.getPerfilSync().executeAsOneOrNull()
        return if (estudiante != null) Json.encodeToString(estudiante) else ""
    }

    override suspend fun getCargaAcademica(): String {
        val data = db.cargaAcademicaQueries.getCargaAcademicaSync().executeAsList()
        return Json.encodeToString(data)
    }

    override suspend fun getKardex(l: Int): String {
        val data = db.kardexQueries.getKardexSync().executeAsList()
        return Json.encodeToString(data)
    }

    override suspend fun getCalificacionesUnidad(): String {
        val data = db.calificacionesQueries.getCalificacionesUnidadSync().executeAsList()
        return Json.encodeToString(data)
    }

    override suspend fun getCalificacionesFinales(): String {
        val data = db.calificacionesQueries.getCalificacionesFinalesSync().executeAsList()
        return Json.encodeToString(data)
    }
}

// ---------------------------------------------------------------
// Implementación RED — llama al servidor SICENET via Ktor
// Idéntica lógica que NetworSNRepository, sin OkHttp
// ---------------------------------------------------------------
class NetworSNRepository : SNRepository {

    override suspend fun acceso(m: String, p: String): String {
        return try {
            val xmlString = acceso(m, p)  // KtorClient.kt
            if (xmlString.contains("<accesoLoginResult>")) {
                xmlString
                    .substringAfter("<accesoLoginResult>")
                    .substringBefore("</accesoLoginResult>")
            } else ""
        } catch (e: Exception) { "" }
    }

    override suspend fun datos_alumno(): String {
        return try {
            val xmlString = datosAlumno()  // KtorClient.kt
            if (xmlString.contains("<getAlumnoAcademicoWithLineamientoResult>")) {
                xmlString
                    .substringAfter("<getAlumnoAcademicoWithLineamientoResult>")
                    .substringBefore("</getAlumnoAcademicoWithLineamientoResult>")
            } else ""
        } catch (e: Exception) { "" }
    }

    override suspend fun getCargaAcademica(): String {
        return try {
            val xmlString = getCargaAcademica()  // KtorClient.kt
            if (xmlString.contains("<getCargaAcademicaByAlumnoResult>")) {
                xmlString
                    .substringAfter("<getCargaAcademicaByAlumnoResult>")
                    .substringBefore("</getCargaAcademicaByAlumnoResult>")
            } else ""
        } catch (e: Exception) { "" }
    }

    override suspend fun getKardex(l: Int): String {
        return try {
            val xmlString = getKardex(l)  // KtorClient.kt
            if (xmlString.contains("<getAllKardexConPromedioByAlumnoResult>")) {
                xmlString
                    .substringAfter("<getAllKardexConPromedioByAlumnoResult>")
                    .substringBefore("</getAllKardexConPromedioByAlumnoResult>")
            } else ""
        } catch (e: Exception) { "" }
    }

    override suspend fun getCalificacionesUnidad(): String {
        return try {
            val xmlString = getCalificacionesUnidad()  // KtorClient.kt
            if (xmlString.contains("<getCalifUnidadesByAlumnoResult>")) {
                xmlString
                    .substringAfter("<getCalifUnidadesByAlumnoResult>")
                    .substringBefore("</getCalifUnidadesByAlumnoResult>")
            } else ""
        } catch (e: Exception) { "" }
    }

    override suspend fun getCalificacionesFinales(): String {
        return try {
            val xmlString = getCalificacionesFinales()  // KtorClient.kt
            if (xmlString.contains("<getAllCalifFinalByAlumnosResult>")) {
                xmlString
                    .substringAfter("<getAllCalifFinalByAlumnosResult>")
                    .substringBefore("</getAllCalifFinalByAlumnosResult>")
            } else ""
        } catch (e: Exception) { "" }
    }
}
