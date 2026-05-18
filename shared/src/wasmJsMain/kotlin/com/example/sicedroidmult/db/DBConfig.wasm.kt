package com.example.sicedroidmult.db

import app.cash.sqldelight.db.SqlDriver

actual fun createDriver(): SqlDriver {
    error("SQLDelight not implemented for WasmJS")
}
