// ============================================================
// ARCHIVO 1: shared/commonMain/kotlin/.../DB/DBConfig.kt
// expect — declara la función, cada plataforma da su driver
// ============================================================
package com.example.sicedroidmult.DB

import app.cash.sqldelight.db.SqlDriver
import com.example.sicedroidmult.db.AppDatabase

// SQLDelight genera AppDatabase automáticamente desde tus .sq
expect fun createDriver(): SqlDriver

fun createDatabase(): AppDatabase {
    return AppDatabase(createDriver())
}
