import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary) // shared es Library, no Application
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)              // reemplaza ksp + room
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "com.example.sicedroidmult.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    jvm()

    js(IR) {
        browser()
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // ---- Compose Multiplatform (reemplaza Jetpack Compose) ----
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            // ---- Navigation ----
            implementation(libs.navigation.compose)

            // ---- ViewModel (mismo API que antes) ----
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // ---- Ktor (reemplaza Retrofit + OkHttp) ----
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            // ---- Serialización (sin cambios) ----
            implementation(libs.kotlinx.serialization.json)

            // ---- SQLDelight runtime (reemplaza Room) ----
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

            // ---- Coroutines ----
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.navigation.compose)
            implementation(compose.materialIconsExtended)
        }

        androidMain.dependencies {
            // Motor HTTP para Android (OkHttp = mismo que usaba Retrofit)
            implementation(libs.ktor.client.okhttp)
            // Driver SQLite para Android (reemplaza room-compiler/ksp)
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.java.engine)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
            implementation(libs.ktor.client.js.engine)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js.engine)
        }
    }
}

// ---- Configuración SQLDelight ----
// Genera AppDatabase.kt automáticamente desde tus archivos .sq
sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.sicedroidmult.db")
        }
    }
}