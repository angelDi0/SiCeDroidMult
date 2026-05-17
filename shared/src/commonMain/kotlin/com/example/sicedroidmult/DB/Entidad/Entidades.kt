package com.example.sicedroidmult.DB.Entidad

import kotlinx.serialization.Serializable

@Serializable
data class Estudiante(
    val matricula: String = "",
    val nombre: String = "",
    val carrera: String = "",
    val especialidad: String = "",
    val semActual: Int = 0,
    val cdtosAcumulados: Int = 0,
    val cdtosActuales: Int = 0,
    val estatus: String = "",
    val modEducativo: Int = 2,
    val urlFoto: String = "",
    val fechaReins: String = "",
    val lastUpdated: Long = 0L
)

@Serializable
data class CargaAcademica(
    val Materia: String = "",
    val Grupo: String = "",
    val Docente: String = "",
    val CreditosMateria: String = "",
    val EstadoMateria: String = ""
)

@Serializable
data class KardexItem(
    val Materia: String = "",
    val Cdts: String = "",
    val Calif: String = "",
    val Acred: String = ""
)

@Serializable
data class CalificacionesUnidadItem(
    val Materia: String? = null,
    val Grupo: String? = null,
    val C1: String? = null,
    val C2: String? = null,
    val C3: String? = null,
    val C4: String? = null,
    val C5: String? = null,
    val C6: String? = null,
    val C7: String? = null,
    val C8: String? = null,
    val C9: String? = null,
    val C10: String? = null,
    val C11: String? = null,
    val C12: String? = null,
    val C13: String? = null
)

@Serializable
data class CalificacionFinalItem(
    val materia: String = "",
    val grupo: String = "",
    val calif: String = "",
    val acred: String = ""
)
