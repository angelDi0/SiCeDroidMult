package com.example.sicedroidmult

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File
import java.io.PrintStream

fun main() = application {

    val logFile = File(System.getProperty("user.home"), ".sicenet/app.log")
    logFile.parentFile.mkdirs()
    val logStream = PrintStream(logFile)
    System.setOut(logStream)
    System.setErr(logStream)

    println("=== APP INICIADA ===")
    println("OS: ${System.getProperty("os.name")}")
    println("Java: ${System.getProperty("java.version")}")
    Window(
        onCloseRequest = ::exitApplication,
        title = "SiCeDroidMult",
    ) {
        App()
    }
}