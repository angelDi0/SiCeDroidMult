package com.example.sicedroidmult.db

import app.cash.sqldelight.db.SqlDriver

// SQLDelight genera AppDatabase automáticamente desde tus .sq
expect fun createDriver(): SqlDriver

fun createDatabase(): AppDatabase {
    return AppDatabase(createDriver())
}
