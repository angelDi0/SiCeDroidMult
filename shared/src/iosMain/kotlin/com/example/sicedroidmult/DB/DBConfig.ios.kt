// ============================================================
// ARCHIVO 3: shared/iosMain/kotlin/.../DB/DBConfig.kt
// actual iOS — usa NativeSqliteDriver
// ============================================================
package com.example.sicedroidmult.DB

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.sicedroidmult.db.AppDatabase

actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(
        schema = AppDatabase.Schema,
        name = "sicenet.db"
    )
}
