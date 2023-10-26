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
    implementation(project(":core"))
    implementation(project(":ksp-core"))
}
