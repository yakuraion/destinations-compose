plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("publishing-conventions")
}

description = "Core KSP component for Destinations-compose"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    if (project.findProperty("useLocal") == "true") {
        api(project(":core"))
    } else {
        api(libs.destinations.compose.core)
    }

    api(libs.ksp)
    api(libs.kotlinpoet.ksp)
}
