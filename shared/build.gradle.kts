// ============================================================
// shared/build.gradle.kts
// Reemplaza: Room, Retrofit, OkHttp, ksp
// Agrega: SQLDelight, Ktor, Compose Multiplatform
// ============================================================
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary) // shared es Library, no Application
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)              // reemplaza ksp + room
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

        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }

        iosMain.dependencies {
            // Motor HTTP nativo iOS
            implementation(libs.ktor.client.darwin)
            // Driver SQLite nativo iOS
            implementation(libs.sqldelight.native.driver)
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
