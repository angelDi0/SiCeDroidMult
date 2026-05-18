package com.example.sicedroidmult.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmult.db.entidad.*
import com.example.sicedroidmult.data.SNRepository
import com.example.sicedroidmult.network.SNParser
import kotlinx.coroutines.launch

enum class AppScreen {
    None, CargaAcademica, Kardex, CalificacionesUnidad, CalificacionesFinales
}

class SNViewModel(private val repository: SNRepository) : ViewModel() {

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

    init {
        cargarDatosAlumno()
    }

    fun cargarDatosAlumno() {
        // Al iniciar, intentar cargar el perfil desde la base de datos local
        viewModelScope.launch {
            val localProfile = repository.getPerfilLocal()
            if (localProfile != null) {
                alumnoData = localProfile
                matriculaInput = localProfile.matricula
            }
        }
    }

    fun onMatriculaChange(value: String) { matriculaInput = value }
    fun onPasswordChange(value: String) { passwordInput = value }

    fun accesoSN() {
        if (matriculaInput.isBlank() || passwordInput.isBlank()) {
            snUiState = SNUiState.Error("Matrícula y contraseña son requeridas")
            return
        }

        viewModelScope.launch {
            snUiState = SNUiState.Loading
            try {
                // 1. Intentar acceso por RED
                println("DEBUG_SYNC: Intentando login por RED...")
                val result = repository.acceso(matriculaInput, passwordInput)
                println("DEBUG_VIEWMODEL_RESULT: $result")
                
                if (result.contains("\"acceso\":true")) {
                    snUiState = SNUiState.Syncing("Obteniendo datos del alumno...")
                    sincronizarTodo()
                } else if (result.contains("EXCEPTION") || result.contains("CLEARTEXT")) {
                    // Si el "resultado" es en realidad una excepción de red atrapada en el Repository
                    println("DEBUG_SYNC: Error detectado en respuesta de red. Intentando modo local...")
                    intentarAccesoLocal()
                } else {
                    snUiState = SNUiState.Error("Error de acceso: $result")
                }
            } catch (e: Exception) {
                println("DEBUG_VIEWMODEL_OFFLINE_ATTEMPT: Excepción capturada. Intentando modo local...")
                intentarAccesoLocal()
            }
        }
    }

    private suspend fun intentarAccesoLocal() {
        val localProfile = repository.getPerfilLocal()
        
        if (localProfile != null && 
            localProfile.matricula.lowercase() == matriculaInput.lowercase() && 
            localProfile.password == passwordInput) {
            
            println("DEBUG_SYNC: Acceso local exitoso. Cargando datos de DB...")
            alumnoData = localProfile
            cargarListasLocales() 
            snUiState = SNUiState.Success
        } else {
            snUiState = SNUiState.Error("Sin conexión a internet y no se encontraron datos locales coincidentes.")
        }
    }

    private suspend fun cargarListasLocales() {
        println("DEBUG_SYNC: Cargando listas desde DB local...")
        cargaAcademica = repository.getCargaLocal()
        kardex = repository.getKardexLocal()
        calificacionesUnidad = repository.getCalificacionesUnidadLocal()
        calificacionesFinales = repository.getCalificacionesFinalesLocal()
        println("DEBUG_SYNC: Datos locales cargados. Carga: ${cargaAcademica.size}, Kardex: ${kardex.size}")
    }

    private suspend fun sincronizarTodo() {
        try {
            // 1. Datos Alumno
            println("DEBUG_SYNC: Sincronizando Perfil...")
            val jsonAlumno = repository.datos_alumno()
            val estudiante = SNParser.parseEstudiante(jsonAlumno)
            if (estudiante != null) {
                val estudianteConPass = estudiante.copy(
                    matricula = matriculaInput,
                    password = passwordInput,
                    lastUpdated = getCurrentTimeMillis()
                )
                repository.guardarPerfil(estudianteConPass)
                alumnoData = estudianteConPass
                println("DEBUG_SYNC: Perfil sincronizado OK")
            } else {
                println("DEBUG_SYNC: Error al parsear Perfil. JSON: $jsonAlumno")
            }

            // 2. Carga Académica
            println("DEBUG_SYNC: Sincronizando Carga Académica...")
            val jsonCarga = repository.getCargaAcademica()
            val carga = SNParser.parseCarga(jsonCarga)
            println("DEBUG_SYNC: Carga parseada: ${carga.size} items")
            repository.guardarCarga(carga)
            cargaAcademica = carga

            // 3. Kardex
            println("DEBUG_SYNC: Sincronizando Kardex...")
            val lineamiento = alumnoData?.lineamiento ?: 0
            val jsonKardex = repository.getKardex(lineamiento)
            val kardexData = SNParser.parseKardex(jsonKardex)
            println("DEBUG_SYNC: Kardex parseado: ${kardexData.size} items")
            repository.guardarKardex(kardexData)
            kardex = kardexData

            // 4. Calificaciones Unidad
            println("DEBUG_SYNC: Sincronizando Calificaciones Unidad...")
            val jsonCalifU = repository.getCalificacionesUnidad()
            val califU = SNParser.parseCalificacionesUnidad(jsonCalifU)
            println("DEBUG_SYNC: Calif Unidad parseada: ${califU.size} items")
            repository.guardarCalificacionesUnidad(califU)
            calificacionesUnidad = califU

            // 5. Calificaciones Finales
            println("DEBUG_SYNC: Sincronizando Calificaciones Finales...")
            val mod = alumnoData?.modEducativo ?: 0
            val jsonCalifF = repository.getCalificacionesFinales(mod)
            val califF = SNParser.parseCalificacionesFinales(jsonCalifF)
            println("DEBUG_SYNC: Calif Finales parseada: ${califF.size} items")
            repository.guardarCalificacionesFinales(califF)
            calificacionesFinales = califF

            snUiState = SNUiState.Success
            println("DEBUG_SYNC: Sincronización COMPLETADA con éxito")
        } catch (e: Exception) {
            println("DEBUG_SYNC_EXCEPTION: ${e.message}")
            e.printStackTrace()
            snUiState = SNUiState.Error("Error al sincronizar datos: ${e.message}")
        }
    }

    fun onMenuOptionSelected(screen: AppScreen) {
        currentScreen = screen
        // Los datos ya están cargados en memoria y DB tras el login
        // Pero podrías refrescarlos aquí si quisieras
    }

    // Nota: Necesitas una forma multiplataforma de obtener el tiempo
    // Por simplicidad, lo dejamos como 0 o puedes añadir un expect/actual
    private fun getCurrentTimeMillis(): Long = 0L
}
