// ============================================================
// ARCHIVO 2: shared/androidMain/kotlin/.../DB/DBConfig.kt
// actual Android — usa AndroidSqliteDriver (= Room driver)
// ============================================================
package com.example.sicedroidmult.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

// applicationContext se pasa desde DefaultAppContainer igual que antes
lateinit var appContext: Context

actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(
        schema = AppDatabase.Schema,
        context = appContext,
        name = "sicenet.db"
    )
}
