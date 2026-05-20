package com.example.sicedroidmult.data

import com.example.sicedroidmult.db.createDatabase
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
        NetworSNRepository(database)
    }
}
