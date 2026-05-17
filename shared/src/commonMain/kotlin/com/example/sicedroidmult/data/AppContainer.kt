// ============================================================
// shared/commonMain/kotlin/.../data/AppContainer.kt
// Cambios respecto al original:
//   - Sin Context de Android (no existe en commonMain)
//   - Sin OkHttpClient, sin Retrofit, sin SimpleXmlConverterFactory
//   - Usa KtorClient + SQLDelight (createDatabase())
// ============================================================
package com.example.sicedroidmult.data

import com.example.sicedroidmult.DB.createDatabase
import com.example.sicedroidmult.db.AppDatabase

interface AppContainer {
    val snRepository: SNRepository
    val database: AppDatabase
}

class DefaultAppContainer : AppContainer {

    override val database: AppDatabase by lazy {
        createDatabase()  // llama a createDriver() expect/actual
    }

    // Cambia entre NetworSNRepository (red) o DBLocalSNRepository (local)
    override val snRepository: SNRepository by lazy {
        NetworSNRepository()
        // Para usar la DB local: DBLocalSNRepository(database)
    }
}
