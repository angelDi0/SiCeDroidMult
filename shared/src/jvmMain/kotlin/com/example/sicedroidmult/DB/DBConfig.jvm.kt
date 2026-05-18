package com.example.sicedroidmult.DB

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.sicedroidmult.db.AppDatabase
import java.io.File

actual fun createDriver(): SqlDriver {
    val databaseFile = File("sicenet.db")
    val isNew = !databaseFile.exists() || databaseFile.length() == 0L
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${databaseFile.absolutePath}")
    
    if (isNew) {
        AppDatabase.Schema.create(driver)
    }

    return driver
}
