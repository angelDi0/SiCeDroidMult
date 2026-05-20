package com.example.sicedroidmult.data

import com.example.sicedroidmult.db.AppDatabase
import com.example.sicedroidmult.db.entidad.*
import com.example.sicedroidmult.network.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface SNRepository {
    suspend fun acceso(m: String, p: String): String
    suspend fun datos_alumno(): String
    suspend fun getCargaAcademica(): String
    suspend fun getKardex(l: Int = 0): String
    suspend fun getCalificacionesUnidad(): String
    suspend fun getCalificacionesFinales(mod: Int): String
    
    // Métodos para guardar en DB
    suspend fun guardarPerfil(estudiante: Estudiante)
    suspend fun guardarCarga(carga: List<CargaAcademica>)
    suspend fun guardarKardex(kardex: List<KardexItem>)
    suspend fun guardarCalificacionesUnidad(califs: List<CalificacionesUnidadItem>)
    suspend fun guardarCalificacionesFinales(califs: List<CalificacionFinalItem>)
    
    // Métodos para leer de DB
    suspend fun getPerfilLocal(): Estudiante?
    suspend fun getCargaLocal(): List<CargaAcademica>
    suspend fun getKardexLocal(): List<KardexItem>
    suspend fun getCalificacionesUnidadLocal(): List<CalificacionesUnidadItem>
    suspend fun getCalificacionesFinalesLocal(): List<CalificacionFinalItem>
}

// ---------------------------------------------------------------
// Implementación RED — llama al servidor SICENET via Ktor
// Idéntica lógica que NetworSNRepository, sin OkHttp
// ---------------------------------------------------------------
class NetworSNRepository(private val db: AppDatabase) : SNRepository {

    override suspend fun acceso(m: String, p: String): String {
        return try {
            val xmlString = networkAcceso(m, p)  // KtorClient.kt
            println("DEBUG_LOGIN_RESPONSE: $xmlString") // Log para depuración
            if (xmlString.contains("<accesoLoginResult>")) {
                xmlString
                    .substringAfter("<accesoLoginResult>")
                    .substringBefore("</accesoLoginResult>")
            } else {
                "ERROR_XML_FORMAT: $xmlString"
            }
        } catch (e: Exception) { 
            println("DEBUG_LOGIN_EXCEPTION: ${e.message}")
            "EXCEPTION: ${e.message}" 
        }
    }

    override suspend fun datos_alumno(): String {
        return try {
            val xmlString = networkDatosAlumno()  // KtorClient.kt
            println("DEBUG_DATOS_ALUMNO_RESPONSE: $xmlString")
            if (xmlString.contains("<getAlumnoAcademicoWithLineamientoResult>")) {
                xmlString
                    .substringAfter("<getAlumnoAcademicoWithLineamientoResult>")
                    .substringBefore("</getAlumnoAcademicoWithLineamientoResult>")
            } else ""
        } catch (e: Exception) { 
            println("DEBUG_DATOS_ALUMNO_EXCEPTION: ${e.message}")
            "" 
        }
    }

    override suspend fun getCargaAcademica(): String {
        return try {
            val xmlString = networkGetCargaAcademica()  // KtorClient.kt
            val result = if (xmlString.contains("<getCargaAcademicaByAlumnoResult>")) {
                xmlString
                    .substringAfter("<getCargaAcademicaByAlumnoResult>")
                    .substringBefore("</getCargaAcademicaByAlumnoResult>")
            } else ""
            println("DEBUG_CARGA_ACADEMICA_JSON: $result")
            result
        } catch (e: Exception) { 
            println("DEBUG_CARGA_ACADEMICA_EXCEPTION: ${e.message}")
            "" 
        }
    }

    override suspend fun getKardex(l: Int): String {
        return try {
            val xmlString = networkGetKardex(l)  // KtorClient.kt
            println("DEBUG_KARDEX_RESPONSE: $xmlString")
            if (xmlString.contains("<getAllKardexConPromedioByAlumnoResult>")) {
                xmlString
                    .substringAfter("<getAllKardexConPromedioByAlumnoResult>")
                    .substringBefore("</getAllKardexConPromedioByAlumnoResult>")
            } else ""
        } catch (e: Exception) { 
            println("DEBUG_KARDEX_EXCEPTION: ${e.message}")
            "" 
        }
    }

    override suspend fun getCalificacionesUnidad(): String {
        return try {
            val xmlString = networkGetCalificacionesUnidad()  // KtorClient.kt
            println("DEBUG_CALIF_UNIDAD_RESPONSE: $xmlString")
            if (xmlString.contains("<getCalifUnidadesByAlumnoResult>")) {
                xmlString
                    .substringAfter("<getCalifUnidadesByAlumnoResult>")
                    .substringBefore("</getCalifUnidadesByAlumnoResult>")
            } else ""
        } catch (e: Exception) { 
            println("DEBUG_CALIF_UNIDAD_EXCEPTION: ${e.message}")
            "" 
        }
    }

    override suspend fun getCalificacionesFinales(mod: Int): String {
        return try {
            val xmlString = networkGetCalificacionesFinales(mod)  // KtorClient.kt
            println("DEBUG_CALIF_FINAL_RESPONSE: $xmlString")
            if (xmlString.contains("<getAllCalifFinalByAlumnosResult>")) {
                xmlString
                    .substringAfter("<getAllCalifFinalByAlumnosResult>")
                    .substringBefore("</getAllCalifFinalByAlumnosResult>")
            } else ""
        } catch (e: Exception) { 
            println("DEBUG_CALIF_FINAL_EXCEPTION: ${e.message}")
            "" 
        }
    }

    override suspend fun guardarPerfil(estudiante: Estudiante) {
        db.estudianteQueries.insertarDatosPerfil(
            estudiante.matricula, estudiante.password, estudiante.fechaReins,
            estudiante.modEducativo.toLong(), if (estudiante.adeudo) 1L else 0L,
            estudiante.urlFoto, estudiante.adeudoDescripcion, if (estudiante.inscrito) 1L else 0L,
            estudiante.estatus, estudiante.semActual.toLong(), estudiante.cdtosAcumulados.toLong(),
            estudiante.cdtosActuales.toLong(), estudiante.especialidad, estudiante.carrera,
            estudiante.lineamiento.toLong(), estudiante.nombre, estudiante.lastUpdated
        )
    }

    override suspend fun guardarCarga(carga: List<CargaAcademica>) {
        db.cargaAcademicaQueries.transaction {
            db.cargaAcademicaQueries.eliminarTodo()
            carga.forEach { item ->
                db.cargaAcademicaQueries.insertarCargaAcademica(
                    item.Semipresencial, item.Observaciones, item.Docente, item.clvOficial,
                    item.Sabado, item.Viernes, item.Jueves, item.Miercoles, item.Martes,
                    item.Lunes, item.EstadoMateria, item.CreditosMateria.toLong(),
                    item.Materia, item.Grupo
                )
            }
        }
    }

    override suspend fun guardarKardex(kardex: List<KardexItem>) {
        db.kardexQueries.transaction {
            db.kardexQueries.eliminarTodo()
            kardex.forEach { item ->
                db.kardexQueries.insertarKardex(
                    item.S3, item.P3, item.A3, item.ClvMat, item.ClvOfiMat, item.Materia,
                    item.Cdts.toLong(), item.Calif.toLong(), item.Acred, item.S1, item.P1,
                    item.A1, item.S2, item.P2, item.A2
                )
            }
        }
    }

    override suspend fun guardarCalificacionesUnidad(califs: List<CalificacionesUnidadItem>) {
        db.calificacionesQueries.transaction {
            db.calificacionesQueries.eliminarTodo()
            califs.forEach { item ->
                db.calificacionesQueries.insertarCalificacionesUnidad(
                    item.Observaciones, item.C13, item.C12, item.C11, item.C10, item.C9,
                    item.C8, item.C7, item.C6, item.C5, item.C4, item.C3, item.C2, item.C1,
                    item.UnidadesActivas, item.Materia, item.Grupo
                )
            }
        }
    }

    override suspend fun guardarCalificacionesFinales(califs: List<CalificacionFinalItem>) {
        db.calificacionesQueries.transaction {
            db.calificacionesQueries.eliminarTodoFinal()
            califs.forEach { item ->
                db.calificacionesQueries.insertarCalificacionFinal(
                    item.calif.toLong(), item.acred, item.grupo, item.materia, item.Observaciones
                )
            }
        }
    }

    override suspend fun getPerfilLocal(): Estudiante? {
        val row = db.estudianteQueries.getPerfilSync().executeAsOneOrNull() ?: return null
        return Estudiante(
            matricula = row.matricula,
            password = row.password,
            fechaReins = row.fechaReins,
            modEducativo = row.modEducativo.toInt(),
            adeudo = row.adeudo == 1L,
            urlFoto = row.urlFoto,
            adeudoDescripcion = row.adeudoDescripcion,
            inscrito = row.inscrito == 1L,
            estatus = row.estatus,
            semActual = row.semActual.toInt(),
            cdtosAcumulados = row.cdtosAcumulados.toInt(),
            cdtosActuales = row.cdtosActuales.toInt(),
            especialidad = row.especialidad,
            carrera = row.carrera,
            lineamiento = row.lineamiento.toInt(),
            nombre = row.nombre,
            lastUpdated = row.lastUpdated
        )
    }

    override suspend fun getCargaLocal(): List<CargaAcademica> {
        return db.cargaAcademicaQueries.getCargaAcademicaSync().executeAsList().map { row ->
            CargaAcademica(
                Semipresencial = row.Semipresencial,
                Observaciones = row.Observaciones,
                Docente = row.Docente,
                clvOficial = row.clvOficial,
                Sabado = row.Sabado,
                Viernes = row.Viernes,
                Jueves = row.Jueves,
                Miercoles = row.Miercoles,
                Martes = row.Martes,
                Lunes = row.Lunes,
                EstadoMateria = row.EstadoMateria,
                CreditosMateria = row.CreditosMateria.toInt(),
                Materia = row.Materia,
                Grupo = row.Grupo
            )
        }
    }

    override suspend fun getKardexLocal(): List<KardexItem> {
        return db.kardexQueries.getKardexSync().executeAsList().map { row ->
            KardexItem(
                S3 = row.S3, P3 = row.P3, A3 = row.A3, ClvMat = row.ClvMat,
                ClvOfiMat = row.ClvOfiMat, Materia = row.Materia, Cdts = row.Cdts.toInt(),
                Calif = row.Calif.toInt(), Acred = row.Acred, S1 = row.S1, P1 = row.P1,
                A1 = row.A1, S2 = row.S2, P2 = row.P2, A2 = row.A2
            )
        }
    }

    override suspend fun getCalificacionesUnidadLocal(): List<CalificacionesUnidadItem> {
        return db.calificacionesQueries.getCalificacionesUnidadSync().executeAsList().map { row ->
            CalificacionesUnidadItem(
                Observaciones = row.Observaciones ?: "", C13 = row.C13 ?: "", C12 = row.C12 ?: "",
                C11 = row.C11 ?: "", C10 = row.C10 ?: "", C9 = row.C9 ?: "", C8 = row.C8 ?: "",
                C7 = row.C7 ?: "", C6 = row.C6 ?: "", C5 = row.C5 ?: "", C4 = row.C4 ?: "",
                C3 = row.C3 ?: "", C2 = row.C2 ?: "", C1 = row.C1 ?: "",
                UnidadesActivas = row.UnidadesActivas ?: "", Materia = row.Materia ?: "",
                Grupo = row.Grupo ?: ""
            )
        }
    }

    override suspend fun getCalificacionesFinalesLocal(): List<CalificacionFinalItem> {
        return db.calificacionesQueries.getCalificacionesFinalesSync().executeAsList().map { row ->
            CalificacionFinalItem(
                calif = row.calif.toInt(), acred = row.acred, grupo = row.grupo,
                materia = row.materia, Observaciones = row.Observaciones
            )
        }
    }
}

