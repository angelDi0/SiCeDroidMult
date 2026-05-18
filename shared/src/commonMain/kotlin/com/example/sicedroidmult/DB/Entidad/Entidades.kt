package com.example.sicedroidmult.db.entidad

import kotlinx.serialization.Serializable

// Sin @Entity ni @PrimaryKey de Room — SQLDelight maneja la DB
// @Serializable se mantiene igual

@Serializable
data class Estudiante(
    val matricula: String,
    val password: String = "",
    val fechaReins: String = "",
    val modEducativo: Int = 0,
    val adeudo: Boolean = false,
    val urlFoto: String = "",
    val adeudoDescripcion: String = "",
    val inscrito: Boolean = false,
    val estatus: String = "",
    val semActual: Int = 0,
    val cdtosAcumulados: Int = 0,
    val cdtosActuales: Int = 0,
    val especialidad: String = "",
    val carrera: String = "",
    val lineamiento: Int = 0,
    val nombre: String = "",
    val lastUpdated: Long = 0L  // System.currentTimeMillis() no es igual en iOS; se asigna en el repo
)

@Serializable
data class CargaAcademica(
    val id: Long = 0,
    val Semipresencial: String = "",
    val Observaciones: String = "",
    val Docente: String = "",
    val clvOficial: String = "",
    val Sabado: String = "",
    val Viernes: String = "",
    val Jueves: String = "",
    val Miercoles: String = "",
    val Martes: String = "",
    val Lunes: String = "",
    val EstadoMateria: String = "",
    val CreditosMateria: Int = 0,
    val Materia: String = "",
    val Grupo: String = "",
)

@Serializable
data class KardexItem(
    val id: Long = 0,
    val S3: String? = null,
    val P3: String? = null,
    val A3: String? = null,
    val ClvMat: String = "",
    val ClvOfiMat: String = "",
    val Materia: String = "",
    val Cdts: Int = 0,
    val Calif: Int = 0,
    val Acred: String = "",
    val S1: String? = null,
    val P1: String? = null,
    val A1: String? = null,
    val S2: String? = null,
    val P2: String? = null,
    val A2: String? = null,
)

@Serializable
data class CalificacionesUnidadItem(
    val id: Long = 0,
    val Observaciones: String? = "",
    val C13: String? = null,
    val C12: String? = null,
    val C11: String? = null,
    val C10: String? = null,
    val C9: String? = null,
    val C8: String? = null,
    val C7: String? = null,
    val C6: String? = null,
    val C5: String? = null,
    val C4: String? = null,
    val C3: String? = null,
    val C2: String? = null,
    val C1: String? = null,
    val UnidadesActivas: String? = "",
    val Materia: String? = "",
    val Grupo: String? = "",
)

@Serializable
data class CalificacionFinalItem(
    val id: Long = 0,
    val calif: Int = 0,
    val acred: String = "",
    val grupo: String = "",
    val materia: String = "",
    val Observaciones: String = "",
)
