package com.example.sicedroidmult.network

import com.example.sicedroidmult.db.entidad.*
import kotlinx.serialization.json.*

/**
 * Utilidad manual para extraer datos del JSON contenido en el XML de SICENET.
 * SICENET devuelve el JSON como un string dentro de una etiqueta XML.
 */
object SNParser {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun parseEstudiante(jsonString: String): Estudiante? {
        return try {
            json.decodeFromString<Estudiante>(jsonString)
        } catch (e: Exception) {
            null
        }
    }

    fun parseCarga(jsonString: String): List<CargaAcademica> {
        return try {
            // Log para ver qué llega exactamente al parser de Carga
            println("DEBUG_PARSER: Intentando parsear Carga Académica. Longitud: ${jsonString.length}")
            json.decodeFromString<List<CargaAcademica>>(jsonString)
        } catch (e: Exception) {
            println("DEBUG_PARSER: Error en parseCarga: ${e.message}")
            emptyList()
        }
    }

    fun parseKardex(jsonString: String): List<KardexItem> {
        return try {
            // El JSON de Kardex viene envuelto en un objeto: {"lstKardex": [...]}
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            val listElement = jsonObject["lstKardex"] ?: return emptyList()
            json.decodeFromJsonElement<List<KardexItem>>(listElement)
        } catch (e: Exception) {
            println("DEBUG_PARSER: Error en parseKardex: ${e.message}")
            emptyList()
        }
    }

    fun parseCalificacionesUnidad(jsonString: String): List<CalificacionesUnidadItem> {
        return try {
            json.decodeFromString<List<CalificacionesUnidadItem>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parseCalificacionesFinales(jsonString: String): List<CalificacionFinalItem> {
        return try {
            json.decodeFromString<List<CalificacionFinalItem>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
