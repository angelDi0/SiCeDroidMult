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
