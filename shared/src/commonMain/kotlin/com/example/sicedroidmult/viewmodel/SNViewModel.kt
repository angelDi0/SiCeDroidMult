package com.example.sicedroidmult.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmult.DB.Entidad.*
import kotlinx.coroutines.launch

enum class AppScreen {
    None, CargaAcademica, Kardex, CalificacionesUnidad, CalificacionesFinales
}

class SNViewModel : ViewModel() {

    var snUiState: SNUiState by mutableStateOf(SNUiState.Idle)
        private set

    var matriculaInput: String by mutableStateOf("")
        private set

    var passwordInput: String by mutableStateOf("")
        private set

    var alumnoData: Estudiante? by mutableStateOf(null)
        private set

    var currentScreen: AppScreen by mutableStateOf(AppScreen.None)
        private set

    var cargaAcademica: List<CargaAcademica> by mutableStateOf(emptyList())
        private set

    var kardex: List<KardexItem> by mutableStateOf(emptyList())
        private set

    var calificacionesUnidad: List<CalificacionesUnidadItem> by mutableStateOf(emptyList())
        private set

    var calificacionesFinales: List<CalificacionFinalItem> by mutableStateOf(emptyList())
        private set

    fun onMatriculaChange(value: String) { matriculaInput = value }
    fun onPasswordChange(value: String) { passwordInput = value }

    fun accesoSN() {
        viewModelScope.launch {
            snUiState = SNUiState.Loading
            // Simulación — aquí conectarás tu lógica real
            snUiState = SNUiState.Success
        }
    }

    fun cargarDatosAlumno() {
        viewModelScope.launch {
            alumnoData = Estudiante(
                matricula = matriculaInput,
                nombre = "Juan Pérez López",
                carrera = "Ingeniería en Sistemas Computacionales",
                especialidad = "Desarrollo de Software",
                semActual = 6,
                cdtosAcumulados = 180,
                cdtosActuales = 24,
                estatus = "Regular",
                modEducativo = 2,
                urlFoto = "",
                fechaReins = "15/08/2025",
                lastUpdated = 0L
            )
        }
    }

    fun onMenuOptionSelected(screen: AppScreen) {
        currentScreen = screen
        viewModelScope.launch {
            when (screen) {
                AppScreen.CargaAcademica -> cargarCargaAcademica()
                AppScreen.Kardex -> cargarKardex()
                AppScreen.CalificacionesUnidad -> cargarCalifUnidad()
                AppScreen.CalificacionesFinales -> cargarCalifFinales()
                else -> {}
            }
        }
    }

    private suspend fun cargarCargaAcademica() {
        cargaAcademica = listOf(
            CargaAcademica("Desarrollo Móvil", "A", "Prof. García", "6", "Cursando")
        )
    }

    private suspend fun cargarKardex() {
        kardex = listOf(
            KardexItem("Programación", "6", "9.5", "Sí")
        )
    }

    private suspend fun cargarCalifUnidad() {
        calificacionesUnidad = listOf(
            CalificacionesUnidadItem(Materia = "Redes", Grupo = "A", C1 = "9", C2 = "8")
        )
    }

    private suspend fun cargarCalifFinales() {
        calificacionesFinales = listOf(
            CalificacionFinalItem("Cálculo", "B", "8.0", "Sí")
        )
    }
}