import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm()
    
    sourceSets {
        jvmMain.dependencies {
            implementation(projects.shared)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.compose.uiToolingPreview)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.example.sicedroidmult.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example.sicedroidmult"
            packageVersion = "1.0.0"
        }
    }
}
