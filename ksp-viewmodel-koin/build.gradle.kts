plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("publishing-conventions")
}

description = "ViewModel-Koin KSP component for Destinations-compose"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    if (project.findProperty("useLocal") == "true") {
        implementation(project(":ksp-core"))
    } else {
        implementation(libs.destinations.compose.ksp.core)
    }
}
