import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
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

    @OptIn(ExperimentalWasmDsl::class)
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
            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            
            // UI Tooling
            implementation(libs.compose.uiToolingPreview)

            // Navigation
            implementation(libs.navigation.compose)

            // Lifecycle
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Ktor & Serialization
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)

            // SQLDelight
            api(libs.sqldelight.runtime)
            api(libs.sqldelight.coroutines.extensions)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.java.engine)
            api(libs.sqldelight.sqlite.driver)
        }

        jsMain.dependencies {
            implementation(libs.wrappers.browser)
            implementation(libs.ktor.client.js.engine)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js.engine)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.sicedroidmult.db")
        }
    }
}
